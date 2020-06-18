package com.example.petspal.PetProfileFrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.petspal.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [Basic.newInstance] factory method to
 * create an instance of this fragment.
 */
class Basic : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val key = arguments?.getString(ARG_KEY)
        android.util.Log.d("BasicKey", key)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic, container, false)
    }

    companion object {
        const val ARG_KEY = "key"

        fun newInstance(key: String): Basic {
            val fragment = Basic()

            val bundle = Bundle().apply {
                putString(ARG_KEY, key)
            }

            fragment.arguments = bundle

            return fragment
        }
    }
}
