package com.example.petspal.Onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.petspal.DashboardClient
import com.example.petspal.R
import kotlinx.android.synthetic.main.page_three.*
import me.tylerbwong.allaboard.builder.onboarding
import me.tylerbwong.allaboard.builder.page

class Onboarding : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val navigator = onboarding {
            backgroundColor = R.color.Gray
            showIndicator = true
           //eListener = ... // Set an onPageChangeListener to listen to page changes
           // navigationView = ... // Set a custom navigation view to handle switching pages

            page {
                customViewRes = R.layout.page_one // Or just pass a layout resource id!
            }

            page {
                customViewRes = R.layout.page_two // Or just pass a layout resource id!
            }

            page {
                customViewRes = R.layout.page_three // Or just pass a layout resource id!

            }


            onFinish {
                Toast.makeText(this@Onboarding, "We're all done here!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@Onboarding, DashboardClient::class.java))
                finish()
            }
        }

    }

    fun addPet(view:View) {
        startActivity(Intent(this@Onboarding, AddPet::class.java))
    }
}

