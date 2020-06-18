package com.example.petspal.PetProfileFrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.petspal.R

/**
 * A simple [Fragment] subclass.
 */
class Details : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val key = arguments?.getString(ARG_KEY)
        android.util.Log.d("DetKey", key)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    companion object {
        const val ARG_KEY = "key"

        fun newInstance(key: String): Details {
            val fragment = Details()

            val bundle = Bundle().apply {
                putString(ARG_KEY, key)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

}
