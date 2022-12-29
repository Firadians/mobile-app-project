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

import com.example.kenangin.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment(), View.OnClickListener {

    lateinit var binding : FragmentRegisterBinding
    lateinit var auth : FirebaseAuth
    lateinit var store: FirebaseFirestore
    private lateinit var ref: DocumentReference

    private var firebaseUserID: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()
        binding.registerButton.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fragmentLoginButton -> {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            R.id.fragmentRegisterButton -> {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            R.id.registerButton -> {
                registerUser()
            }
        }
    }

    private fun registerUser() {
        val email = binding.emailText.text.toString()
        val nama = binding.usernameText.text.toString()
        val password = binding.passwordRegisterText.text.toString()
        val konfirmasiPassword = binding.retypeRegisterText.text.toString()

        if(password == konfirmasiPassword) {
            if (email == "") {
                Toast.makeText(this.context, "Please fill out all fields", Toast.LENGTH_LONG).show()
            } else if (nama == "") {
                Toast.makeText(this.context, "Please fill out all fields", Toast.LENGTH_LONG).show()
            } else if (password == "") {
                Toast.makeText(this.context, "Please fill out all fields", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this.context, "Registering...", Toast.LENGTH_LONG).show()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseUserID = auth.currentUser!!.uid
                            ref = store.collection("Users").document(firebaseUserID)

                            val userHashMap = HashMap<String, Any>()
                            userHashMap["uid"] = firebaseUserID
                            userHashMap["nama"] = nama
                            userHashMap["email"] = email

                            ref.set(userHashMap).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("RegisterFragment", "SignUp:success")
                                    val intent = Intent(this.context, homeActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Log.w("RegisterFragment", "SignUp:failure", task.exception)
                                    Toast.makeText(
                                        requireContext(), "SignUp failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Log.w("RegisterFragment", "SignUp:failure", task.exception)
                            Toast.makeText(
                                requireContext(), "SignUp failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }else{
            Toast.makeText(this.context, "Password and Retype Password not same.", Toast.LENGTH_LONG).show()
        }

    }

}