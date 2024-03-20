package com.sukase.sukame.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.usecase.auth.AuthUseCase
import com.sukase.core.utils.UiText
import com.sukase.sukame.R
import com.sukase.sukame.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authUseCase: AuthUseCase) :
    BaseViewModel() {
    private val _isSuccess = MutableLiveData<Boolean?>()
    val isSuccess: LiveData<Boolean?> = _isSuccess

    init {
        _isSuccess.postValue(false)
    }

    fun register(username: String, fullName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authUseCase.register(username, fullName).collectLatest {
                when (it) {
                    is DomainResource.Loading -> {
                        _eventMessage.send(UiText.StringResource(R.string.loading))
                    }

                    is DomainResource.Empty -> {
                        _eventMessage.send(UiText.StringResource(R.string.empty))
                    }

                    is DomainResource.Success -> {
                        _isSuccess.postValue(it.data)
                        _eventMessage.send(UiText.StringResource(R.string.success))
                    }

                    is DomainResource.Error -> {
                        _errorMessage.send(it.error)
                    }

                    else -> {
                        _errorMessage.send(UiText.StringResource(R.string.unknown_error))
                    }
                }
            }
        }
    }
}