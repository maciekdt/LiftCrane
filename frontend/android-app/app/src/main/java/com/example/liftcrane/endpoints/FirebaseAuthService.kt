package com.example.liftcrane.endpoints

import android.util.Log
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthService {

    private val auth = Firebase.auth

    fun signInGoogle(idToken:String){
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MyInfo", "signInWithCredential:success")
                    val user = auth.currentUser
                } else {
                    Log.w("MyInfo", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun isUserSignIn():Boolean {
        return auth.currentUser != null
    }

    fun signOut(){
        auth.signOut()
    }
}