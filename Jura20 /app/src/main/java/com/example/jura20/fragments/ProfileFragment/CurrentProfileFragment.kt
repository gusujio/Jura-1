package com.example.jura20.fragments.ProfileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.jura20.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class CurrentProfileFragment() : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var logout: Button
    lateinit var emailCurrentProfileFragment: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_current_profile, container, false)
        logout = root.findViewById(R.id.logout_btn)
        auth = FirebaseAuth.getInstance()
        emailCurrentProfileFragment = root.findViewById(R.id.email_tv)
        emailCurrentProfileFragment.text = auth.currentUser?.email
        logout.setOnClickListener {
            auth.signOut()
            val nextFragment = RegisterProfileFragment()
            (activity as FragmentActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, nextFragment)
                .commit()
        }
        return root
    }

    companion object {

    }
}