package com.sukase.sukame.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sukase.core.domain.model.ChatModel
import com.sukase.sukame.databinding.ActivityChatBinding
import com.sukase.sukame.ui.base.showSnackBar
import com.sukase.sukame.ui.base.showToast
import com.sukase.sukame.ui.utils.EditTextUtils
import com.sukase.sukame.ui.utils.NavigationUtils.EXTRA_CONVERSATION_ID
import com.sukase.sukame.ui.utils.NavigationUtils.EXTRA_CONVERSATION_NAME
import com.sukase.sukame.ui.utils.NavigationUtils.EXTRA_CONVERSATION_TOKEN
import com.sukase.sukame.ui.utils.NavigationUtils.EXTRA_CONVERSATION_UID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val chatViewModel: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.rvChat.layoutManager = layoutManager

        val conversationId = intent.getStringExtra(EXTRA_CONVERSATION_ID)
        val token = intent.getStringExtra(EXTRA_CONVERSATION_TOKEN)
        val uid = intent.getStringExtra(EXTRA_CONVERSATION_UID)
        val name = intent.getStringExtra(EXTRA_CONVERSATION_NAME)

        binding.tvTitle.text = name

        Log.d("chat", "$conversationId , $token, $uid")
        if (!conversationId.isNullOrBlank() && !token.isNullOrBlank() && !uid.isNullOrBlank()) {
            Log.d("chat", "masuk if")
            chatViewModel.apply {
                getChatList(token, conversationId)
                data.observe(this@ChatActivity) {
                    Log.d("chat", "masuk observe ${it.filterNotNull()}")
                    showData(it.filterNotNull(), uid)
                }
            }
            binding.apply {
                if (EditTextUtils.isNotBlankEdtField(enterMsg)) {
                    flot.setOnClickListener {
                        Log.d("chat", "masuk click")
                        chatViewModel.sendChat(token, conversationId.toString(), enterMsg.text.toString())
                        enterMsg.setText("")
                        enterMsg.error = null
                    }
                }
            }
        }

        chatViewModel.eventMessage.onEach {
            showToast(this, it)
        }.launchIn(lifecycleScope)

        chatViewModel.errorMessage.onEach {
            showSnackBar(binding.root, this, it)
        }.launchIn(lifecycleScope)
    }

    private fun showData(data: List<ChatModel>, uid: String) {
        val adapter = ChatAdapter(uid)
        adapter.differ.submitList(data.toMutableList())
        binding.rvChat.adapter = adapter
    }
}