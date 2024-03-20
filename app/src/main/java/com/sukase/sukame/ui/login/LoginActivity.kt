package com.sukase.sukame.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.sukase.sukame.databinding.ActivityLoginBinding
import com.sukase.sukame.ui.base.showSnackBar
import com.sukase.sukame.ui.base.showToast
import com.sukase.sukame.ui.conversation.ConversationActivity
import com.sukase.sukame.ui.register.RegisterActivity
import com.sukase.sukame.ui.utils.EditTextUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginViewModel.isSuccess.observe(this) {
            if (it == true) {
                startActivity(Intent(this, ConversationActivity::class.java))
            }
        }

        loginViewModel.eventMessage.onEach {
            showToast(this, it)
        }.launchIn(lifecycleScope)

        loginViewModel.errorMessage.onEach {
            showSnackBar(binding.root, this, it)
        }.launchIn(lifecycleScope)

        binding.apply {
            loginButton.setOnClickListener {
                if (EditTextUtils.isNotBlankEdtField(edtUsername, edtFullName)) {
                    loginViewModel.login(
                        edtUsername.text.toString(),
                        edtFullName.text.toString()
                    )
                } else {
                    loginViewModel.setBlankFieldError()
                }
            }
        }
    }
}