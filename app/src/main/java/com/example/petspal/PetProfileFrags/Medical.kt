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
class Medical : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medical, container, false)
    }

    companion object {
        const val ARG_KEY = "key"

        fun newInstance(key: String): Medical {
            val fragment = Medical()

            val bundle = Bundle().apply {
                putString(ARG_KEY, key)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

}
