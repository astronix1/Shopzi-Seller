package com.example.shopziseller.model

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    fun login(email:String, password: String, onComplete: (Boolean, String?)->Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, it.exception?.localizedMessage)
                }
            }
    }




}