package com.example.petspal.PetProfileFrags

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.petspal.Onboarding.Pet

import com.example.petspal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mikepenz.iconics.Iconics.applicationContext
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_basic.*
import kotlinx.android.synthetic.main.fragment_details.*
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Details : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val key = arguments?.getString(ARG_KEY)
        updateUI(key)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val key = arguments?.getString(ARG_KEY)
        details_edit_info.setOnClickListener {
            val intent = Intent(activity, DetailsEditInfo::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

    }



    private fun updateUI(key: String?) {
        val userKey = FirebaseAuth.getInstance().currentUser?.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val petQuery: Query = rootRef.child("pets").child(userKey.toString()).child(key!!)
        petQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }


            @SuppressLint("NewApi")
            override fun onDataChange(petsSnapshot: DataSnapshot) {
                try {
                    val pet = petsSnapshot.getValue(Pet::class.java)
                    if (pet != null) {


                        details_in_out_info.text = "Indoor"
                        details_food_type.text = "Home made"
                        details_meal_size.text = "Medium"
                        details_meals_per_day.text = "3"
                        details_traveling.text = "Frequently"
                        details_physical_act.text = "Medium"
                    }


                } catch (Ex: Exception) {

                }
            }
        })
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
