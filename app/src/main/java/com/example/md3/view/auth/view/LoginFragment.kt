package com.example.md3.view.auth.view


import Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.md3.R
import com.example.md3.databinding.FragmentLoginBinding
import com.example.md3.utils.Progressive
import com.example.md3.utils.ViewUtils
import com.example.md3.view.auth.AuthActivity
import com.example.md3.view.auth.viewmodel.AuthViewModel
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() ,  Progressive{

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel : AuthViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listeners()
        observe()

    }

    private fun observe() {
        authViewModel.loginResult.observe(viewLifecycleOwner){
            if(!it.hasBeenHandled){
                when(it.status){
                    Status.SUCCESS -> {
                        show(false)
                        it.data?.let { it1 ->
                            (requireActivity() as AuthActivity).apply {
                                setAuthTokensAndAuthData(it1)
                                authViewModel.getUserProfile()
                            }
                        }
                    }
                    Status.ERROR -> {
                        show(false)
                        ViewUtils.showSnackbarAtTop(binding.imageView,it.message)
                    }
                    Status.LOADING -> {
                        show(true,)
                    }
                }
            }
        }


        authViewModel.getUserProfileLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    show(false)
                    (requireActivity() as AuthActivity).apply {
                        setOrgUserID(it.data)
                        startMainActivity()
                    }
                }

                Status.ERROR -> {
                    show(false)
                    ViewUtils.showToast(requireContext(), it.message)
                }

                Status.LOADING -> {
                    show(true,)

                }
            }
        }


    }




    private fun listeners() {
        binding.apply {
            btnLayout.btnLogin.setOnClickListener {
                if(validate()){
                    val email = binding.emailInput.text.toString().trim()
                    val password = binding.passwordInput.text.toString().trim()
                    authViewModel.loginWithEmailAndPassword(email, password)
                }
            }

            tvLoginWithOtp.setOnClickListener {
                findNavController().navigate(R.id.action_global_loginWithOtpFragment)
            }
        }


    }

    private fun initView() {
        binding.btnLayout.btnLogin.text = getString(R.string.login)
    }

    private fun validate(): Boolean {
        val username = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

        var isValid = true

        if (username.isEmpty()) {
            binding.emailInputLayout.error = "Username is required"
            isValid = false
        } else {
            binding.emailInputLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passwordInputLayout.error = "Password is required"
            isValid = false
        } else {
            binding.passwordInputLayout.error = null
        }

        return isValid
    }

    override fun show(show: Boolean) {
        binding.btnLayout.apply {
            if (show) {
                btnLogin.isClickable = false
                progressBar.isVisible = true
                btnLogin.text = ""
            } else {
                btnLogin.isClickable = true
                progressBar.isVisible = false
                btnLogin.text = getString(R.string.login)
            }
        }
    }


}



