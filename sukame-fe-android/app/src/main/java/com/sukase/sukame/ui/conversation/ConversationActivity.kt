package com.sukase.sukame.ui.conversation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.model.ConversationModel
import com.sukase.core.domain.model.UserModel
import com.sukase.sukame.databinding.ActivityConversationBinding
import com.sukase.sukame.ui.base.showSnackBar
import com.sukase.sukame.ui.base.showToast
import com.sukase.sukame.ui.chat.ChatActivity
import com.sukase.sukame.ui.login.LoginActivity
import com.sukase.sukame.ui.utils.NavigationUtils.EXTRA_CONVERSATION_ID
import com.sukase.sukame.ui.utils.NavigationUtils.EXTRA_CONVERSATION_NAME
import com.sukase.sukame.ui.utils.NavigationUtils.EXTRA_CONVERSATION_TOKEN
import com.sukase.sukame.ui.utils.NavigationUtils.EXTRA_CONVERSATION_UID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConversationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConversationBinding
    private val conversationViewModel: ConversationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvConversation.layoutManager = layoutManager

        conversationViewModel.user.observe(this) { user ->
            Log.d("conversation", "user $user")
            if (user != null) {
                Log.d("conversation", "masuk conversation")
                conversationViewModel.getConversationList(user.token).onEach {
                    when (it) {
                        is DomainResource.Loading -> {
                            showLoading(true)
                        }

                        is DomainResource.Empty -> {
                            showLoading(false)
                        }

                        is DomainResource.Success -> {
                            showLoading(false)
                            showData(it.data.filterNotNull(), user)
                        }

                        is DomainResource.Error -> {
                            showLoading(false)
                        }

                        else -> {
                            showLoading(false)
                        }
                    }
                }.launchIn(lifecycleScope)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        conversationViewModel.eventMessage.onEach {
            showToast(this, it)
        }.launchIn(lifecycleScope)

        conversationViewModel.errorMessage.onEach {
            showSnackBar(binding.root, this, it)
        }.launchIn(lifecycleScope)
    }

    private fun showData(data: List<ConversationModel>, user: UserModel) {
        val adapter = ConversationAdapter()
        Log.d("chat", "$user")
        adapter.setOnItemClickCallback(object : ConversationAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ConversationModel) {
                val intent = Intent(this@ConversationActivity, ChatActivity::class.java)
                intent.putExtra(EXTRA_CONVERSATION_ID, data.id)
                    .putExtra(EXTRA_CONVERSATION_TOKEN, user.token)
                    .putExtra(EXTRA_CONVERSATION_UID, user.id)
                    .putExtra(EXTRA_CONVERSATION_NAME, data.name)
                startActivity(intent)
            }

            override fun onProfileClicked(profile: String) {
                Log.d("conversation", "Success $profile")
            }
        })
        adapter.differ.submitList(data.toMutableList())
        binding.rvConversation.adapter = adapter
    }

    private fun showLoading(it: Boolean) {
        binding.apply {
            progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
            rvConversation.visibility = if (it) View.INVISIBLE else View.VISIBLE
        }
    }
}