package com.example.test6.ui.register

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.test6.R
import com.example.test6.databinding.RegistrationFragmentBinding
import com.example.test6.ui.base.BaseFragment
import com.example.test6.util.ResponseHandler
import kotlinx.coroutines.launch

class RegistrationFragment :
    BaseFragment<RegistrationFragmentBinding>(RegistrationFragmentBinding::inflate) {

    private val viewModel: RegistrationViewModel by viewModels()


    override fun start() {
        listeners()
        observer()
    }


    private fun listeners() {
        binding.btnRegistration.setOnClickListener {
            viewModel.register(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etConfirmPassword.text.toString()
            )
        }

        binding.vBack.setOnClickListener {
            findNavController().navigate(
                RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment(
                    binding.etEmail.text.toString(),
                    binding.etConfirmPassword.text.toString()
                )
            )
        }

    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect {
                    when (it) {
                        is ResponseHandler.Success -> {
                            Toast.makeText(
                                context,
                                getString(R.string.registered_successfully) + it.data.token,
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigate(
                                RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment(
                                    binding.etEmail.text.toString(),
                                    binding.etConfirmPassword.text.toString()
                                )
                            )
                        }
                        is ResponseHandler.Failure -> {
                            Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        is ResponseHandler.Loading -> {
                            Toast.makeText(context, getString(R.string.loading), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}