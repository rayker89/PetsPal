package com.example.petspal.PetProfileFrags

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petspal.Onboarding.Pet
import com.example.petspal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_basic.*
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [Basic.newInstance] factory method to
 * create an instance of this fragment.
 */
class Basic : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val key = arguments?.getString(ARG_KEY)
        updateUI(key)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic, container, false)
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
                        val myFormat = "MM/dd/yyyy" // mention the format you need
                        val formatter = DateTimeFormatter.ofPattern(myFormat, Locale.ENGLISH)
                        val dateOfBirth = LocalDate.parse(pet.dateOfBirth, formatter)
                        val currentDate = LocalDate.now()
                        val pet_age_days = Period.between(dateOfBirth, currentDate).days
                        val pet_age_years = Period.between(dateOfBirth, currentDate).years

                        if (pet_age_years != 0) {
                            if (pet_age_years == 1) {
                                if (pet_age_days == 1) {
                                    basic_profile_age.text =
                                        pet_age_years.toString() + " year " + pet_age_days.toString() + " day"
                                } else {
                                    basic_profile_age.text =
                                        pet_age_years.toString() + " year " + pet_age_days.toString() + " days"
                                }

                            } else {
                                if (pet_age_days == 1) {
                                    basic_profile_age.text =
                                        pet_age_years.toString() + " years " + pet_age_days.toString() + " day"
                                }
                                basic_profile_age.text =
                                    pet_age_years.toString() + " years " + pet_age_days.toString() + " days"

                            }

                        } else {
                            if(pet_age_days == 1) {
                                basic_profile_age.text = pet_age_days.toString() + " day"
                            } else {
                                basic_profile_age.text = pet_age_days.toString() + " days"
                            }

                        }

                        basic_profile_dob.text = pet.dateOfBirth
                        basic_profile_name.text = pet.name
                        basic_profile_species.text = pet.species
                        basic_profile_breed.text = pet.breed
                        basic_profile_weight.text = pet.weight + " Kg"
                        Picasso.get().load(pet.image).into(basic_profile_photo)
                    }


                } catch (Ex: Exception) {

                }
            }
        })
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
