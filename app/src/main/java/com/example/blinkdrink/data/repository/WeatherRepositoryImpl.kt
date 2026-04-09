// ============================================================================
// FILE: data/repository/WeatherRepositoryImpl.kt
// ============================================================================
package com.example.blinkdrink.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.blinkdrink.data.mapper.WeatherMapper
import com.example.blinkdrink.data.source.remote.WeatherRemoteDataSource
import com.example.blinkdrink.domain.model.WeatherInfo
import com.example.blinkdrink.domain.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val geocoder: Geocoder
) : WeatherRepository {

    private var lastKnownCity: String? = null
    private var lastKnownLocation: Location? = null

    companion object {
        private const val CITY_CHANGE_THRESHOLD_KM = 5.0
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentWeather(): Result<WeatherInfo> {
        return try {
            if (!hasLocationPermission()) {
                return Result.failure(SecurityException("Không có quyền truy cập vị trí"))
            }

            // Lấy vị trí hiện tại
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).await() // await() là hàm treo của Coroutines để đợi kết quả

            if (location == null) {
                return Result.failure(Exception("Không thể lấy vị trí hiện tại"))
            }

            fetchWeatherForLocation(location) // Gọi API thời tiết với tọa độ này
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @SuppressLint("MissingPermission")
    override fun observeLocationChanges(): Flow<Result<WeatherInfo>> = callbackFlow {
        if (!hasLocationPermission()) {
            trySend(Result.failure(SecurityException("Không có quyền truy cập vị trí")))
            close()
            return@callbackFlow
        }

        // ✅ FIX: LocationRequest với minUpdateDistanceMeters = 0
        // Điều này buộc nhận mọi thay đổi GPS từ giả lập
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L // Mỗi 10 giây
        ).apply {
            setMinUpdateIntervalMillis(5000L) // Tối thiểu 5 giây
            setMaxUpdateDelayMillis(15000L) // Tối đa 15 giây
            setMinUpdateDistanceMeters(0f) // ⭐ QUAN TRỌNG: Nhận mọi update
            setWaitForAccurateLocation(false) // Không đợi GPS chính xác
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { newLocation ->
                    android.util.Log.d(
                        "WeatherRepo",
                        "📍 GPS Update: ${newLocation.latitude}, ${newLocation.longitude}"
                    )

                    if (hasCityChanged(newLocation)) {
                        android.util.Log.d("WeatherRepo", "🌍 Thành phố thay đổi - Cập nhật thời tiết")

                        launch {
                            val weatherResult = kotlin.runCatching {
                                fetchWeatherForLocation(newLocation)
                            }.getOrElse {
                                Result.failure(it)
                            }

                            lastKnownLocation = newLocation
                            trySend(weatherResult)
                        }
                    } else {
                        android.util.Log.d("WeatherRepo", "📌 Vẫn trong cùng khu vực")
                    }
                }
            }
        }

        try {
            // Bắt đầu lắng nghe GPS
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            ).await()

            // Lấy vị trí ban đầu
            fusedLocationClient.lastLocation.await()?.let { location ->
                val initialWeather = fetchWeatherForLocation(location)
                lastKnownLocation = location
                trySend(initialWeather)
            }
        } catch (e: SecurityException) {
            trySend(Result.failure(e))
            close(e)
            return@callbackFlow
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private suspend fun fetchWeatherForLocation(location: Location): Result<WeatherInfo> {
        return try {
            val cityName = getCityNameFromLocation(location.latitude, location.longitude)
            lastKnownCity = cityName

            val weatherResult = weatherRemoteDataSource.getCurrentWeather(
                latitude = location.latitude,
                longitude = location.longitude
            )

            weatherResult.mapCatching { dto ->
                WeatherMapper.toDomain(dto, cityName)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun hasCityChanged(newLocation: Location): Boolean {
        val lastLocation = lastKnownLocation ?: return true

        val distance = calculateDistance(
            lastLocation.latitude, lastLocation.longitude,
            newLocation.latitude, newLocation.longitude
        )

        return distance > CITY_CHANGE_THRESHOLD_KM
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun getCityNameFromLocation(latitude: Double, longitude: Double): String {
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                address.adminArea ?: address.locality ?: "Việt Nam"
            } else {
                "Việt Nam"
            }
        } catch (e: Exception) {
            "Việt Nam"
        }
    }
}