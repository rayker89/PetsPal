package com.example.petspal.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query


class PetViewModel : ViewModel() {
    val key = FirebaseAuth.getInstance().currentUser?.uid
    val rootRef = FirebaseDatabase.getInstance().reference
    private val HOT_STOCK_REF = rootRef.child("pets").child(key.toString())

    private val liveData: FirebaseQueryLiveData = FirebaseQueryLiveData(HOT_STOCK_REF)

    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return liveData
    }
}