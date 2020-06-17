package com.example.petspal.DashboardFrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.petspal.PetProfileFrags.*
import com.example.petspal.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_dashboard_client.*


/**
 * A simple [Fragment] subclass.
 */
class Pet_profile : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pet_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textview = activity!!.findViewById<View>(R.id.nav_title) as TextView
        textview.text = "Pet Profile"

        val viewPager: ViewPager = view.findViewById(R.id.viewpager)
        val tabLayout: TabLayout = view.findViewById(R.id.tablayout)


        // attach tablayout with viewpager
        tabLayout.setupWithViewPager(viewPager)

        val adapter = ViewPagerAdapter(childFragmentManager)



        // add your fragments
        adapter.addFrag(Basic(), "Basic")
        adapter.addFrag(Details(), "Details")
        adapter.addFrag(Medical(), "Medical")
        adapter.addFrag(Gallery(), "Gallery")
        adapter.addFrag(Log(), "Log")

        // set adapter on viewpager

        // set adapter on viewpager
        viewPager.adapter = adapter
    }


}
