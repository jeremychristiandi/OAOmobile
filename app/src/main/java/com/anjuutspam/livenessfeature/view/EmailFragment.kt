package com.anjuutspam.livenessfeature.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anjuutspam.livenessfeature.databinding.FragmentEmailBinding

class EmailFragment : Fragment() {

    private var _binding: FragmentEmailBinding? = null
    private val binding get() = _binding!!
//    private lateinit var apiService: ApiService (BELUM DIPAKE KARENA BELUM ADA APINYA nanti harus buat apinya dulu!)
    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener {
//            val email = binding.inputRegEmail.text.toString()
//            val otp = binding.inputRegOtp.text.toString()
//            if(email.isNotEmpty() && otp.isNotEmpty()) {
//                verifyEmail(email,otp)
//            }else{
//                Toast.makeText(context, "Masukkan email dan Kode OTP anda", Toast.LENGTH_SHORT).show()
//            }
            mainActivity?.navigateToLiveFragment()
        }
    }

//    private fun verifyEmail(email: String, otp: String) {
//        val request = Verification Request (email, otp)
//        apiService.verifyEmail(request).enqueue(object : Callback<VerificationResponse> {
//            override fun onResponse(
//                call: Call<VerificationResponse>,
//                response: Response<VerificationResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val verificationResponse = response.body()
//                    if (verificationResponse != null && verificationResponse.success) {
//                        mainActivity?.navigateToLiveFragment()
//                    } else {
//                        Toast.makeText(
//                            context,
//                            "Verifikasi Gagal: ${verificationResponse?.Message}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    Toast.makeText(
//                        context,
//                        "Verifikasi gagal: ${response.message()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
//                Toast.makeText(context, "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }


        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}