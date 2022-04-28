package com.example.liftcrane.endpoints

import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
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
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("MyInfo", "signInWithCredential:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("MyInfo", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun signOut(){
        auth.signOut()
    }



}