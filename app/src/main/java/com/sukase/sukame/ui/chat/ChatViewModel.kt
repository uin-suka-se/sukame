package com.sukase.sukame.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.model.ChatModel
import com.sukase.core.domain.usecase.chat.ChatUseCase
import com.sukase.core.utils.UiText
import com.sukase.sukame.R
import com.sukase.sukame.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatUseCase: ChatUseCase) : BaseViewModel() {

    private val _data = MutableLiveData<List<ChatModel?>>()
    val data: LiveData<List<ChatModel?>> = _data

    fun getChatList(token: String, conversationId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatUseCase.getChatList(token, conversationId).collectLatest {
                when (it) {
                    is DomainResource.Loading -> {
                        _eventMessage.send(UiText.StringResource(R.string.loading))
                    }

                    is DomainResource.Empty -> {
                        _eventMessage.send(UiText.StringResource(R.string.empty))
                    }

                    is DomainResource.Success -> {
                        _data.postValue(it.data.filterNotNull())
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

    fun sendChat(token: String, conversationId: String, message: String) {
        chatUseCase.sendChat(token, conversationId, message).onEach {
            when (it) {
                is DomainResource.Loading -> {
                    _eventMessage.send(UiText.StringResource(R.string.loading))
                }

                is DomainResource.Empty -> {
                    _eventMessage.send(UiText.StringResource(R.string.empty))
                }

                is DomainResource.Success -> {
                    _data.postValue(listOf(it.data).plus(_data.value ?: emptyList()))
                    Log.d("chat", "${listOf(it.data).plus(_data.value ?: emptyList())}")
                    _eventMessage.send(UiText.StringResource(R.string.success))
                }

                is DomainResource.Error -> {
                    _errorMessage.send(it.error)
                }

                else -> {
                    _errorMessage.send(UiText.StringResource(R.string.unknown_error))
                }
            }
        }.launchIn(viewModelScope)
    }
}