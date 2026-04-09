// ============================================================================
// FILE: presentation/tips/TipsViewModel.kt
// ============================================================================
package com.example.blinkdrink.presentation.tips

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TipsViewModel @Inject constructor(
    // Không cần inject gì vì dữ liệu là tĩnh
) : ViewModel() {
    // ViewModel này có thể để trống hoặc thêm logic sau này
    // Ví dụ: tracking xem user đã đọc tip nào, favorite tips, etc.
}