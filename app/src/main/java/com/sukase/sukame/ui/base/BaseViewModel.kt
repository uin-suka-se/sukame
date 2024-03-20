package com.sukase.sukame.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukase.core.utils.UiText
import com.sukase.sukame.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {
    protected val _eventMessage: Channel<UiText> = Channel()
    val eventMessage: Flow<UiText> = _eventMessage.receiveAsFlow()

    protected val _errorMessage: Channel<UiText> = Channel()
    val errorMessage: Flow<UiText> = _errorMessage.receiveAsFlow()

    fun setBlankFieldError() {
        viewModelScope.launch {
            _errorMessage.send(UiText.StringResource(R.string.blank_edt_field_error))
        }
    }
}