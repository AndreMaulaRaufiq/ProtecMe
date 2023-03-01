package com.hermes.protecme.model.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hermes.protecme.model.Users

class MainViewModel:ViewModel() {

    private val _user = MutableLiveData<Users?>()
    val user = _user

    fun getUser(uid:String){
        val dbUser= FirebaseDatabase.getInstance().getReference("users").orderByChild("id").equalTo(uid)
        dbUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (value: DataSnapshot in snapshot.children){
                        val user = value.getValue(Users::class.java)
                        _user.value = user
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}