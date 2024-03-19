package com.example.uma_authz_smartphone.ui.manage

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ManageViewModel(
): ViewModel() {
    private val _uiState = MutableStateFlow(ManageUiState())
    val uiState = _uiState.asStateFlow()
}