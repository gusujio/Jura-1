package com.example.jura20.fragments.HomeFragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.jura20.R

/**
 * A simple [Fragment] subclass.
 */
class SwipeFragment : Fragment() {

    lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_swipe, container, false)
        textView = view.findViewById(R.id.text_swipe)
        textView.text = (arguments?.getString("text"))
        return view
    }

}
