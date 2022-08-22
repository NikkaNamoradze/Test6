package com.example.test6.ui.home

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.test6.constants.Constants
import com.example.test6.databinding.HomeFragmentBinding
import com.example.test6.ui.base.BaseFragment
import com.example.test6.ui.login.dataStore
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    private val args: HomeFragmentArgs by navArgs()

    override fun start() {
        binding.tvUserEmail.text = args.email
        listener()
    }

    private fun listener(){
        binding.btnLogOut.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            viewLifecycleOwner.lifecycleScope.launch {
                context?.dataStore?.edit {
                    it[Constants.userToken] = ""
                    it[Constants.isRemembered] = false
                }
            }
        }
    }
}