package com.example.petspal

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.petspal.DashboardFrags.Alerts
import com.example.petspal.DashboardFrags.Calendar
import com.example.petspal.DashboardFrags.Home
import com.example.petspal.DashboardFrags.Pet_profile
import com.example.petspal.Onboarding.AddPet
import com.example.petspal.Onboarding.Pet
import com.example.petspal.ViewModels.PetViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mikepenz.iconics.utils.parseXmlAndSetIconicsDrawables
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
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_dashboard_client.*


class DashboardClient : AppCompatActivity() {
    val item1 = PrimaryDrawerItem().withIdentifier(1).withName("HOME").withSelectable(false)
    val item2 = SecondaryDrawerItem().withIdentifier(2).withName("SETTINGS").withSelectable(false)
    var petsList = ArrayList<Pet>()
    var slider:MaterialDrawerSliderView? = null
    var currentPetProfile:Pet? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_client)


        val nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val menu = nav.menu
        bottom_nav.itemIconTintList = null
        val profile = menu.findItem(R.id.add_pet_icon)
        val add_pet = Pet_profile()
        val home = Home()
        val calendar = Calendar ()
        val alerts = Alerts()

        val viewModel = ViewModelProvider(this)[PetViewModel::class.java]
        val liveData = viewModel.getDataSnapshotLiveData()

        liveData.observe(this, Observer {
            if (it!=null){
                val pets = ArrayList<Pet>()
                for (petSnapshot in it!!.children) {
                    val pet = petSnapshot.getValue(Pet::class.java)
                    pets.add(pet!!)
                }
                Log.d("PETVAL",slider?.accountHeader?.activeProfile?.icon?.uri.toString())
                pets.forEach {
                    if(slider?.accountHeader?.activeProfile?.icon?.uri.toString() == it.image )
                        Picasso.get().load(it.image).into(object : Target {

                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                // loaded bitmap is here (bitmap)
                                val scaled = Bitmap.createScaledBitmap(bitmap,200,200,false)
                                profile.setIcon(BitmapDrawable(nav.resources, scaled))
                            }

                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {

                            }

                        })
                        currentPetProfile=it
                }

            }

        })


        val fragmentChanger = supportFragmentManager.beginTransaction()
        fragmentChanger.replace(R.id.frame_layout, home)
        nav.selectedItemId = R.id.home_icon
        fragmentChanger.commit()
        nav.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.add_pet_icon -> {
                    if(currentPetProfile==null){
                        startActivity(Intent(this@DashboardClient, AddPet::class.java))
                        finish()
                        true
                    }
                    else {
                        val fragmentChanger = supportFragmentManager.beginTransaction()
                        fragmentChanger.replace(R.id.frame_layout, add_pet)
                        fragmentChanger.commit()
                        true
                    }

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
                                        .withIcon(it.image.toString()).withContentDescription(it.key)

                                )
                            }


                        }



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


