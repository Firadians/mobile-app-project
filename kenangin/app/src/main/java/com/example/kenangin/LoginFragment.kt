package com.example.kenangin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.kenangin.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var store: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()
        binding.loginButton.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.registerButton -> {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            R.id.fragmentRegisterButton -> {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            R.id.loginButton -> {
                manualSignIn()
            }
        }
    }

    private fun manualSignIn() {
        val email = binding.usernameText.text.toString()
        val password = binding.passwordLoginText.text.toString()
        if (email == "" && password == "") {
            Toast.makeText(
                requireContext(),
                "Please fill Email and Password",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            Toast.makeText(this.context, "Login...", Toast.LENGTH_LONG).show()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginFragment", "signInWithEmail:success")
                        Toast.makeText(
                            requireContext(), "Authentication succeed.",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this.context, homeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w("LoginFragment", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(), "Authentication failed.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }


}