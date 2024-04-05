package com.example.mappproject

import android.util.Log
import com.google.firebase.auth.FirebaseAuth


private val auth = FirebaseAuth.getInstance()

fun register(username: String, password: String) {
    auth.createUserWithEmailAndPassword(username, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _goToNext.value = true
            } else {
            }
            _goToNext.value = false
            Log.d("Error", "Error creating user: ${task.result}")
            modifiyProcessing()
        }
}


fun login(username: String?, password: String?) {
    auth.signInWithEmailAndPassword (username!!, password!!)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _userId.value = task.result.user?.uid
                _loggedUser.value = task.result.user?.email?.split("@")?.get(0)
                _goToNext.value = true
            } else {
            }
            _goToNext.value = false
            Log.d("Error", "Error signing in: ${task.result}")
            modifiyProcessing()
        }
}
*/
