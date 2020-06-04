package com.example.petspal

import android.graphics.drawable.Drawable
import android.icu.lang.UCharacter.GraphemeClusterBreak
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.petspal.DashboardFrags.*
import com.example.petspal.Onboarding.Pet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mikepenz.fastadapter.FastAdapter.Companion.with
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import com.squareup.picasso.Picasso


class DashboardClient : AppCompatActivity() {
    val item1 = PrimaryDrawerItem().withIdentifier(1).withName("HOME").withSelectable(false)
    val item2 = SecondaryDrawerItem().withIdentifier(2).withName("SETTINGS").withSelectable(false)
    var petsList = ArrayList<Pet>()
    var slider:MaterialDrawerSliderView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_client)


        val nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val add_pet = Pet_profile()
        val home = Home()
        val calendar = Calendar ()
        val alerts = Alerts()

        val fragmentChanger = supportFragmentManager.beginTransaction()
        fragmentChanger.replace(R.id.frame_layout, home)
        nav.selectedItemId = R.id.home_icon
        fragmentChanger.commit()
        nav.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.add_pet_icon -> {

                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    fragmentChanger.replace(R.id.frame_layout, add_pet)
                    fragmentChanger.commit()
                    true
                }

                R.id.calendar_icon -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    fragmentChanger.replace(R.id.frame_layout, calendar)
                    fragmentChanger.commit()
                    true
                }

                R.id.home_icon -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    fragmentChanger.replace(R.id.frame_layout, home)
                    fragmentChanger.commit()
                    true
                }

                R.id.alerts_icon -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    fragmentChanger.replace(R.id.frame_layout, alerts)
                    fragmentChanger.commit()
                    true
                }

                else -> false
            }
        }

        slider = findViewById(R.id.slider)
        AddItems()

        checkPets()

        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
                Picasso.get().load(uri).placeholder(placeholder).into(imageView)
            }

            override fun cancel(imageView: ImageView) {
                Picasso.get().cancelRequest(imageView)
            }
        } )

    }


    fun checkPets() {
            val key = FirebaseAuth.getInstance().currentUser?.uid
            val rootRef = FirebaseDatabase.getInstance().reference
            val petQuery: Query = rootRef.child("pets").child(key.toString())
            petQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(petsSnapshot: DataSnapshot) {
                    try{
                        val pets = ArrayList<Pet>()
                        for (petSnapshot in petsSnapshot.children) {
                            val pet = petSnapshot.getValue(Pet::class.java)
                            pets.add(pet!!)
                        }
                        petsList = pets
                        val headerView = AccountHeaderView(this@DashboardClient).apply {

                            attachToSliderView(slider!!) // attach to the slider
                            petsList.forEach {
                                addProfiles(
                                    ProfileDrawerItem().withName(it.name).withEmail(it.breed)
                                        .withIcon(it.image.toString())
                                )
                            }

                        }
                        Log.d("PETVALUE", petsList.elementAt(0).name)
                    }catch (Ex:Exception) {

                    }

                }
            })

    }

    private fun AddItems() {
        slider?.itemAdapter?.add(
            item1,
            DividerDrawerItem(),
            item2,
            SecondaryDrawerItem().withName("FAQ").withSelectable(false)
        )



// specify a click listener
        slider?.onDrawerItemClickListener = { v, drawerItem, position ->
            // do something with the clicked item :D
            false
        }
    }

    fun GetMenu(view: View) {

        slider?.drawerLayout?.openDrawer(slider!!)
    }

    override fun onBackPressed() {
        if(slider?.drawerLayout?.isDrawerOpen(slider!!)!!){
        slider?.drawerLayout?.closeDrawer(slider!!)}
        else {
            super.onBackPressed()
        }


    }




}
