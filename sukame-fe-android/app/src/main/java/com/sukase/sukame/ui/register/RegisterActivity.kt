package com.sukase.sukame.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sukase.sukame.databinding.ActivityRegisterBinding
import com.sukase.sukame.ui.base.showSnackBar
import com.sukase.sukame.ui.base.showToast
import com.sukase.sukame.ui.login.LoginActivity
import com.sukase.sukame.ui.utils.EditTextUtils.isNotBlankEdtField
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel.isSuccess.observe(this) {
            if (it == true) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        registerViewModel.eventMessage.onEach {
            showToast(this, it)
        }.launchIn(lifecycleScope)

        registerViewModel.errorMessage.onEach {
            showSnackBar(binding.root, this, it)
        }.launchIn(lifecycleScope)

        binding.apply {
            registerButton.setOnClickListener {
                if (isNotBlankEdtField(edtUsername, edtFullName)) {
                    registerViewModel.register(
                        edtUsername.text.toString(),
                        edtFullName.text.toString()
                    )
                } else {
                    registerViewModel.setBlankFieldError()
                }
            }
        }
    }
}