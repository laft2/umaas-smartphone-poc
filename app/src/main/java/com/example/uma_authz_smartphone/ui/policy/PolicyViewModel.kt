package com.example.uma_authz_smartphone.ui.policy

import androidx.lifecycle.ViewModel
import com.example.uma_authz_smartphone.ui.manage.ManageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PolicyViewModel(
): ViewModel() {
    private val _uiState = MutableStateFlow(ManageUiState())
    val uiState = _uiState.asStateFlow()


}