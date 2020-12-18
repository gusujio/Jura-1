package com.example.jura20.fragments.ProfileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.jura20.R
import com.google.firebase.auth.FirebaseAuth


class LoginProfileFragment : Fragment() {
    lateinit var login: Button
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login_profile, container, false)
        login = root.findViewById(R.id.login_user_btn)
        email = root.findViewById(R.id.email_edit_text)
        password = root.findViewById(R.id.password_edit_text)
        auth = FirebaseAuth.getInstance()

        login.setOnClickListener {

            if(email.text.trim().toString().isNotEmpty() && password.text.trim().toString().isNotEmpty()){
                signInUser(email.text.trim().toString(), password.text.trim().toString())
            }
            else{
                Toast.makeText(context, "Input uncorrect data", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }
    fun signInUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val nextFragment = CurrentProfileFragment()
                    (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, nextFragment)
                        .commit()
                }
                else{
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
    companion object {
    }
}