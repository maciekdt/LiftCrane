package com.example.liftcrane.endpoints

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthService {

    private val auth = Firebase.auth

    fun signIn(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("MyInfo", "signInWithEmail:success")
                    val user = auth.currentUser
                } else {
                    Log.w("MyInfo", "signInWithEmail:failure", task.exception)
                }
            }
    }

    fun signOut(){
        auth.signOut()
    }
}