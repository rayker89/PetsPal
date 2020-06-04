package com.example.petspal.Account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.petspal.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    var user_email:EditText? = null
    var user_pass:EditText? = null
    var user_name:EditText? = null
    var user_lastName:EditText? = null
    var register_btn:Button? = null
    var client_radio:RadioButton? = null
    var vet_radio:RadioButton? = null
    var firebaseAuth:FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        user_email = findViewById(R.id.user_register_email)
        user_pass = findViewById(R.id.user_register_password)
        user_name = findViewById(R.id.user_register_name)
        user_lastName = findViewById(R.id.user_register_last_name)
        register_btn = findViewById(R.id.register)
        client_radio = findViewById(R.id.register_client_radio)
        vet_radio = findViewById(R.id.register_veterinarian_radio)
        firebaseAuth = FirebaseAuth.getInstance()


        register_btn?.setOnClickListener {

            RegisterUser()
        }
    }

    private fun RegisterUser() {

        val email = user_email?.text.toString().trim()
        val password = user_pass?.text.toString().trim()
        val name = user_name?.text.toString().trim()
        val lastName = user_lastName?.text.toString().trim()
        var role:String? = null


        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(name) && TextUtils.isEmpty(lastName) ) {

            Toast.makeText(applicationContext, "This field, can not be empty!", Toast.LENGTH_SHORT).show()

        } else {

            if(client_radio!!.isChecked) {
                role = "Client"
            } else {
                role = "Vet"
            }

            firebaseAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(object: OnCompleteListener<AuthResult> {
                override fun onComplete(auth: Task<AuthResult>) {

                    if(auth.isSuccessful) {
                        Toast.makeText(applicationContext, "Account created!!!", Toast.LENGTH_SHORT).show()
                        LoadLogin(role)
                    }
                    else {
                        val error = auth.exception?.message
                        Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()

                    }

                }

            })

        }


    }

    private fun LoadLogin(role:String) {
        val email = user_email?.text.toString().trim()
        val name = user_name?.text.toString().trim()
        val lastName = user_lastName?.text.toString().trim()
        val key = firebaseAuth?.currentUser?.uid
        val user = User(name, lastName, role)
        myRef.child("users").child(key.toString()).setValue(user)

        startActivity(Intent(this@Register, LogIn::class.java))

    }
}
