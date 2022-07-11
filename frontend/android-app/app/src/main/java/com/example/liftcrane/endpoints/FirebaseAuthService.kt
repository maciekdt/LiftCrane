package com.example.liftcrane.endpoints

import android.util.Log
import com.example.liftcrane.model.Lift
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthService {

    private val auth = Firebase.auth

    fun signInGoogle(resolve:(userUid : String?) -> Unit,
                     reject:(e : Exception) -> Unit,
                     idToken:String){

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resolve(auth.uid)
                } else {
                    task.exception?.let { reject(it) }
                }
            }
    }

    fun isUserSignIn():Boolean {
        return auth.currentUser != null
    }

    fun signOut(){
        auth.signOut()
    }

    fun getSignInUserUid(): String?{
        return if(isUserSignIn()) auth.uid
        else null
    }
}