package com.example.petspal.Account

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.petspal.DashboardClient
import com.example.petspal.DashboardVet
import com.example.petspal.Onboarding.Loading
import com.example.petspal.Onboarding.Onboarding
import com.example.petspal.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class LogIn : AppCompatActivity() {

    var user_email:EditText? = null
    var user_pass:EditText? = null
    var login_btn:Button? = null
    var firebaseAuth:FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        user_email = findViewById(R.id.user_login_email)
        user_pass = findViewById(R.id.user_login_password)
        login_btn = findViewById(R.id.login)
        firebaseAuth = FirebaseAuth.getInstance()

        login_btn?.setOnClickListener {
            LoginUser()
        }

    }

    private fun LoginUser() {

        var email = user_email?.text.toString().trim()
        var password = user_pass?.text.toString().trim()

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password) ) {

            Toast.makeText(applicationContext, "This field, can not be empty!", Toast.LENGTH_SHORT).show()

        } else {

            firebaseAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(object:OnCompleteListener<AuthResult>{
                override fun onComplete(auth: Task<AuthResult>) {
                    if(auth.isSuccessful) {
                        Toast.makeText(applicationContext, "You are logedin in successfully!", Toast.LENGTH_SHORT).show()
                        LoadDashboard()

                        finish()

                    }else {
                        var error = auth.exception?.message
                        Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()

                    }
                }

            })

        }

    }

    private fun LoadDashboard() {
        val key = firebaseAuth?.currentUser?.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val roleQuery: Query = rootRef.child("users").child(key.toString())
        val petQuery: Query = rootRef.child("pets").child(key.toString())
        var role:String? = null

        roleQuery.addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(roleSnapshot: DataSnapshot) {
                    if(roleSnapshot.exists()) {
                        role = roleSnapshot.child("role").getValue(String::class.java)

                        if(role.equals("Client")){
                            checkPet()
                        } else {
                            startActivity(Intent(this@LogIn, DashboardVet::class.java))
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
                    val i = Intent(this@LogIn, Loading::class.java)
                    i.putExtra("Pet", "true")
                    startActivity(i)
                    finish()
                }else {
                    val i = Intent(this@LogIn, Loading::class.java)
                    i.putExtra("Pet", "false")
                    startActivity(i)
                    finish()
                }

            }

        })


    }
}
