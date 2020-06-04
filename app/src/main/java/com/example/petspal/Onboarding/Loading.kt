package com.example.petspal.Onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.petspal.DashboardClient
import com.example.petspal.R

class Loading : AppCompatActivity() {



        // This is the loading time of the splash screen
        private val SPLASH_TIME_OUT:Long = 3000 // 1 sec
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val pet:String = intent.getStringExtra("Pet")
            setContentView(R.layout.loading_activity)


            Handler().postDelayed({
                // This method will be executed once the timer is over
                // Start your app main activity
                if(pet.equals("true")){
                    startActivity(Intent(this,DashboardClient::class.java))
                } else {
                    startActivity(Intent(this,Onboarding::class.java))

                }

                // close this activity
                finish()
            }, SPLASH_TIME_OUT)
        }

}
