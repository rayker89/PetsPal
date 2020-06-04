package com.example.petspal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.petspal.Account.LogIn
import com.example.petspal.Onboarding.Loading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if(user!=null){
            LoadDashboard()
        } else {
            setContentView(R.layout.activity_main)
        }

    }



    fun Register(view:View) {
        startActivity(Intent(applicationContext, com.example.petspal.Account.Register::class.java))
        finish()
    }

    fun Login(view:View) {
        startActivity(Intent(applicationContext, LogIn::class.java))
        finish()
    }

    private fun LoadDashboard() {
        val key = FirebaseAuth.getInstance().currentUser?.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val roleQuery: Query = rootRef.child("users").child(key.toString())
        var role:String? = null

        roleQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(roleSnapshot: DataSnapshot) {
                if(roleSnapshot.exists()) {
                    role = roleSnapshot.child("role").getValue(String::class.java)
                    if(role.equals("Client")){
                       checkPet()
                    } else {
                        startActivity(Intent(this@MainActivity, DashboardVet::class.java))
                        finish()
                    }
                }

            }

        })




    }

    private fun checkPet() {
        val key = FirebaseAuth.getInstance().currentUser?.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        Log.d("TAG", key.toString())
        val petQuery: Query = rootRef.child("pets").child(key.toString())

        petQuery.addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(petSnapshot: DataSnapshot) {
                Log.d("TAG", petSnapshot.toString())
                if(petSnapshot.exists()) {
                    val i = Intent(this@MainActivity, Loading::class.java)
                    i.putExtra("Pet", "true")
                    startActivity(i)
                    finish()
                }else {
                    val i = Intent(this@MainActivity, Loading::class.java)
                    i.putExtra("Pet", "false")
                    startActivity(i)
                    finish()
                }

            }

        })


    }


}
