package com.example.md3.view.auth.view

import Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.md3.R
import com.example.md3.databinding.FragmentLoginWithOtpBinding
import com.example.md3.utils.Progressive
import com.example.md3.utils.ViewUtils
import com.example.md3.view.auth.AuthActivity
import com.example.md3.view.auth.viewmodel.AuthViewModel
import org.koin.android.ext.android.inject

class LoginWithOtpFragment : Fragment(), Progressive {

    private lateinit var binding: FragmentLoginWithOtpBinding
    private val authViewModel: AuthViewModel by inject()
    private var otpToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginWithOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listeners()
        observe()
    }

    private fun observe() {
        authViewModel.getOtpLoginResult.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        showOtpInput()
                        it.data?.let { otpTokenResponse ->
                            otpToken = otpTokenResponse.otp_token
                        }
                        ViewUtils.showSnackbarAtTop(
                            binding.imageView,
                            getString(R.string.otp_sent_successfully)
                        )
                        binding.btnLayout.btnLogin.text = getString(R.string.continues)
                    }

                    Status.ERROR -> {
                        show(false)
                        binding.btnLayout.btnLogin.text = getString(R.string.get_otp)
                    }

                    Status.LOADING -> {
                        show(true)
                    }
                }
            }
        }


        authViewModel.verifyOtpResult.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        binding.btnLayout.btnLogin.text = getString(R.string.continues)
                        it.data?.let { it1 ->
                            (requireActivity() as AuthActivity).apply {
                                setAuthTokensAndAuthData(it1)
                                authViewModel.getUserProfile()
                            }
                        }
                    }

                    Status.ERROR -> {
                        show(false)
                        ViewUtils.showSnackbarAtTop(binding.imageView, it.message)
                    }

                    Status.LOADING -> {
                        show(true)
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


    private fun hideOtpInput() {
        binding.otpInputLayout.visibility = View.GONE
    }

    private fun showOtpInput() {
        binding.otpInputLayout.visibility = View.VISIBLE
    }


    private fun listeners() {

        binding.usernameInput.doAfterTextChanged { username ->
            if (username.isNullOrBlank()) {
                binding.btnLayout.btnLogin.text = getString(R.string.get_otp)
                hideOtpInput()
            }
        }


        binding.btnLayout.btnLogin.setOnClickListener {
            if (binding.btnLayout.btnLogin.text == getString(R.string.get_otp)) {
                sendOtp()
            } else {
                verifyOtp()
            }
        }
    }

    private fun sendOtp() {
        if (validateEmail()) {
            val username = binding.usernameInput.text.toString().trim()
            authViewModel.loginWithOtp(username)
        }
    }

    private fun verifyOtp() {
        if (validateOTP()) {
            val otp = binding.otpInput.text.toString().trim()
            otpToken?.let {
                authViewModel.verifyOtp(otp, it)
            }
        }
    }

    private fun initView() {
        binding.btnLayout.btnLogin.text = getString(R.string.get_otp)
    }


    private fun validateEmail(): Boolean {
        val email = binding.usernameInput.text.toString().trim()
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

        return if (email.isEmpty() || !email.matches(emailPattern)) {
            binding.usernameInputLayout.error = "Enter a valid email address"
            false
        } else {
            binding.usernameInputLayout.error = null
            true
        }
    }


    private fun validateOTP(): Boolean {
        val otp = binding.otpInput.text.toString().trim()

        if (otp.isEmpty()) {
            binding.otpInputLayout.error = "OTP is required"
            return false
        } else {
            binding.otpInputLayout.error = null
        }
        return true
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
            }
        }
    }
}
