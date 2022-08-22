package com.example.test6.ui.login


import android.content.Context
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.test6.R
import com.example.test6.constants.Constants
import com.example.test6.databinding.LoginFragmentBinding
import com.example.test6.ui.base.BaseFragment
import com.example.test6.util.ResponseHandler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userRemember")

class LoginFragment : BaseFragment<LoginFragmentBinding>(LoginFragmentBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()
    private val args: LoginFragmentArgs by navArgs()


    override fun start() {
        listeners()
        observer()
        setRegisterValues()
        rememberUserRedirect()
    }

    private fun listeners() {
        binding.btnLogIn.setOnClickListener {
            viewModel.logIn(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        binding.btnRegistration.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }

    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logInState.collect {
                    when {
                        it is ResponseHandler.Success -> {
                            findNavController().navigate(
                                LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                                    binding.etEmail.text.toString()
                                )
                            )
                        }
                        it is ResponseHandler.Failure -> {
                            Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        it is ResponseHandler.Loading -> {
                            Toast.makeText(context, getString(R.string.loading), Toast.LENGTH_SHORT)
                                .show()
                        }
                        it is ResponseHandler.Success && binding.cbRememberMe.isChecked -> {

                            val token = it.data.token
                            context?.dataStore?.edit { preference ->
                                preference[Constants.userToken] = token.toString()
                                preference[Constants.isRemembered] = true
                            }

                            findNavController().navigate(
                                LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                                    binding.etEmail.text.toString()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setRegisterValues() {

        when {
            args.registerEmail == getString(R.string.email) && args.registerPassword == getString(R.string.password) -> {
                binding.etEmail.hint = getString(R.string.email)
                binding.etPassword.hint = getString(R.string.password)
            }
            else -> {
                binding.etEmail.setText(args.registerEmail.toString())
                binding.etPassword.setText(args.registerPassword.toString())
            }
        }
    }



    private fun rememberUserRedirect(){
        viewLifecycleOwner.lifecycleScope.launch {

            val user = context?.dataStore?.data?.first()
            val token = user?.get(Constants.userToken)?.toString()
            val rememberMe = user?.get(Constants.isRemembered)

            if (rememberMe == true) {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                        token
                    )
                )
            }
        }
    }

}