package com.example.jura20.fragments.ProfileFragment

import android.os.Bundle
import android.util.Log
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

/**
 * A simple [Fragment] subclass.
 */
class RegisterProfileFragment : Fragment() {
    lateinit var login: Button
    lateinit var register: Button
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var auth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        register = root.findViewById(R.id.login_user_btn)
        login = root.findViewById(R.id.go_login)
        email = root.findViewById(R.id.email_edit_text)
        password = root.findViewById(R.id.password_edit_text)
        auth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            val nextFragment = LoginProfileFragment()
            (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, nextFragment)
                .commit()
        }
        register.setOnClickListener {
            if(email.text.trim().toString().isNotEmpty() && password.text.trim().toString().isNotEmpty()){
                createUser(email.text.trim().toString(), password.text.trim().toString())
            }
            else{
                Toast.makeText(context, "Input uncorrect data", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }
    fun createUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() {task ->
                if(task.isSuccessful){
                    Log.e("Task Message", "Successful......." + task.exception)
                    val nextFragment = CurrentProfileFragment()
                    (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, nextFragment)
                        .commit()
                }else{
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user != null){
            val nextFragment = CurrentProfileFragment()
            (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, nextFragment)
                .commit()
        }
    }

}
