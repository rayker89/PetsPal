package com.example.petspal.Onboarding

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.petspal.DashboardClient
import com.example.petspal.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_pet.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddPet : AppCompatActivity() {

    var name: EditText? = null
    var date: EditText? = null
    val cal = Calendar.getInstance()
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    var firebaseAuth: FirebaseAuth? = null
    var db: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)
        name = findViewById(R.id.addPet_name)
        date = findViewById(R.id.addPet_date_birth)
        addPet_add_btn.isEnabled = false
        storageReference = FirebaseStorage.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        checkFields()
        addPet_image.setOnClickListener { launchGallery() }
        setBirth()
        setSpiciesSpinner()
        setGenderSpinner()
        setBreedSpinner()

        addPet_add_btn.setOnClickListener {
            uploadImage()
        }

    }

    private fun checkFields() {
        addPet_name.addTextChangedListener(mWatcher)
        addPet_date_birth.addTextChangedListener(mWatcher)
        addPet_weight.addTextChangedListener(mWatcher)
        addPet_image.addTextChangedListener(mWatcher)

    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            val fileName = filePath?.getName(applicationContext)

            try {

                addPet_image.setText(fileName)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun Uri.getName(context: Context): String {
        val returnCursor = context.contentResolver.query(this, null, null, null, null)
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val fileName = returnCursor.getString(nameIndex)
        returnCursor.close()
        return fileName
    }

    private fun setBirth() {
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        }

        date!!.setOnClickListener {
            DatePickerDialog(
                this@AddPet,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setBreedSpinner() {
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Breed")
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                // set item text bold
                view.setTypeface(view.typeface, Typeface.BOLD)

                // set selected item style
                if (position == addPet_breed.selectedItemPosition && position != 0) {
                    view.background = ColorDrawable(Color.parseColor("#F7E7CE"))
                    view.setTextColor(Color.parseColor("#333399"))
                }
                // make hint item color gray
                if (position == 0) {
                    view.setTextColor(Color.LTGRAY)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }
        addPet_breed.isEnabled = false
        addPet_breed.adapter = adapter
        addPet_breed.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    if (addPet_name.text.toString() != "" &&
                        addPet_date_birth.text.toString() != "" &&
                        addPet_breed.selectedItem.toString() != "Breeds" &&
                        addPet_gender.selectedItem.toString() != "Gender" &&
                        addPet_spcies.selectedItem.toString() != "Spicies" &&
                        addPet_weight.text.toString() != "" &&
                        addPet_image.text.toString() != ""
                    ) {
                        addPet_add_btn.isEnabled = true
                    }
                }

            }
        }
    }

    private fun setGenderSpinner() {
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Gender", "Female", "Male")
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                // set item text bold
                view.setTypeface(view.typeface, Typeface.BOLD)

                // set selected item style
                if (position == addPet_gender.selectedItemPosition && position != 0) {
                    view.background = ColorDrawable(Color.parseColor("#F7E7CE"))
                    view.setTextColor(Color.parseColor("#333399"))
                }
                // make hint item color gray
                if (position == 0) {
                    view.setTextColor(Color.LTGRAY)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }

        addPet_gender.adapter = adapter
        addPet_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    if (addPet_name.text.toString() != "" &&
                        addPet_date_birth.text.toString() != "" &&
                        addPet_breed.selectedItem.toString() != "Breeds" &&
                        addPet_gender.selectedItem.toString() != "Gender" &&
                        addPet_spcies.selectedItem.toString() != "Spicies" &&
                        addPet_weight.text.toString() != "" &&
                        addPet_image.text.toString() != ""
                    ) {
                        addPet_add_btn.isEnabled = true
                    }
                }

            }
        }
    }

    private fun setSpiciesSpinner() {
        val adapter1: ArrayAdapter<String> = object : ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Species", "Bird", "Cat", "Dog", "Fish", "Horse", "Humster", "Rabbit")
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                // set item text bold
                view.setTypeface(view.typeface, Typeface.BOLD)

                // set selected item style
                if (position == addPet_spcies.selectedItemPosition && position != 0) {
                    view.background = ColorDrawable(Color.parseColor("#F7E7CE"))
                    view.setTextColor(Color.parseColor("#333399"))
                }
                // make hint item color gray
                if (position == 0) {
                    view.setTextColor(Color.LTGRAY)
                }


                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }

        addPet_spcies.adapter = adapter1
        addPet_spcies.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    if (addPet_name.text.toString() != "" &&
                        addPet_date_birth.text.toString() != "" &&
                        addPet_breed.selectedItem.toString() != "Breeds" &&
                        addPet_gender.selectedItem.toString() != "Gender" &&
                        addPet_spcies.selectedItem.toString() != "Spicies" &&
                        addPet_weight.text.toString() != "" &&
                        addPet_image.text.toString() != ""
                    ) {
                        addPet_add_btn.isEnabled = true
                    }
                }

                if (position == 2) {
                    makeBreedsAdapter("Cat")
                }

                if (position == 3) {
                    makeBreedsAdapter("Dog")
                }

            }
        }
    }

    private fun makeBreedsAdapter(breed: String) {

        var list: List<String>? = null

        when (breed) {
            "Dog" -> {
                list = listOf(
                    "Breed",
                    "Alaskan husky",
                    "Boxer",
                    "German Shepherd",
                    "Labrador Retriever",
                    "Maltese",
                    "Poodle",
                    "Pug",
                    "Samoyed"
                )
            }

            "Cat" -> {
                list = listOf("Breed", "Aegean", "Napoleon", "Persian", "Ragdoll", "Russian Blue")
            }
        }


        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            list
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                // set item text bold
                view.setTypeface(view.typeface, Typeface.BOLD)

                // set selected item style
                if (position == addPet_breed.selectedItemPosition && position != 0) {
                    view.background = ColorDrawable(Color.parseColor("#F7E7CE"))
                    view.setTextColor(Color.parseColor("#333399"))
                }
                // make hint item color gray
                if (position == 0) {
                    view.setTextColor(Color.LTGRAY)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }

        addPet_breed.isEnabled = true
        addPet_breed.adapter = adapter
        addPet_breed.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    if (addPet_name.text.toString() != "" &&
                        addPet_date_birth.text.toString() != "" &&
                        addPet_breed.selectedItem.toString() != "Breeds" &&
                        addPet_gender.selectedItem.toString() != "Gender" &&
                        addPet_spcies.selectedItem.toString() != "Spicies" &&
                        addPet_weight.text.toString() != "" &&
                        addPet_image.text.toString() != ""
                    ) {
                        addPet_add_btn.isEnabled = true
                    }

                }

            }


        }
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        date!!.setText(sdf.format(cal.time))
    }

    private val mWatcher: TextWatcher = (object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (addPet_name.text.toString() != "" &&
                addPet_date_birth.text.toString() != "" &&
                addPet_breed.selectedItem.toString() != "Breeds" &&
                addPet_gender.selectedItem.toString() != "Gender" &&
                addPet_spcies.selectedItem.toString() != "Spicies" &&
                addPet_weight.text.toString() != "" &&
                addPet_image.text.toString() != ""
            ) {
                Log.d(
                    "TAG",
                    addPet_name.text.toString() + addPet_date_birth.text + addPet_spcies.selectedItem + addPet_gender.selectedItem + addPet_breed.selectedItem + addPet_weight.text + addPet_image.text
                )
                addPet_add_btn.isEnabled = true
            } else {
                addPet_add_btn.isEnabled = false
            }

        }

        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
        }
    })

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference?.child("pet_images/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask =
                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        addUploadRecordToDb(downloadUri.toString())

                    } else {
                        // Handle failures
                    }
                }?.addOnFailureListener {

                }
        } else {
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUploadRecordToDb(uri: String) {
        var myReference = db?.reference
        val key = firebaseAuth?.currentUser?.uid
        val pet = Pet(
            addPet_name.text.toString(), addPet_date_birth.text.toString(),
            addPet_spcies.selectedItem.toString(), addPet_gender.selectedItem.toString(),
            addPet_breed.selectedItem.toString(), addPet_weight.text.toString(), uri
        )
        myReference?.child("pets")?.child(key.toString())?.push()?.setValue(pet)
            ?.addOnSuccessListener {
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@AddPet, DashboardClient::class.java))
                finish()
            }
            ?.addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }

    }
}
