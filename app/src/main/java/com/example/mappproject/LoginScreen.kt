package com.example.mappproject
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController

private val auth = FirebaseAuth.getInstance()
val _goToNext: MutableLiveData<Boolean> = MutableLiveData()
val _showProgressBar: MutableLiveData<Boolean> = MutableLiveData()

fun modifyProcessing(show: Boolean) {
    _showProgressBar.value = show
}

val _userID: MutableLiveData<String> = MutableLiveData()
val _loggedUser: MutableLiveData<String> = MutableLiveData()

fun register(username: String, password: String) {
    auth.createUserWithEmailAndPassword(username, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _goToNext.value = true
            } else {
                _goToNext.value = false
                Log.d("Error", "Error creating user: ${task.exception}")
                modifyProcessing(false) // Cambié modifiyProcessing a modifyProcessing
            }
        }
}

fun login(username: String?, password: String?) {
    auth.signInWithEmailAndPassword(username!!, password!!)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _userID.value = task.result?.user?.uid // Corregí _userId a _userID
                _loggedUser.value = task.result?.user?.email?.split("@")?.get(0)
                _goToNext.value = true
            } else {
                _goToNext.value = false
                Log.d("Error", "Error signing in: ${task.exception}")
                modifyProcessing(false)
            }
        }
}



    @Composable
    fun LoginScreen(navController: NavController) {
        var username by remember { mutableStateOf("omar") }
        var password by remember { mutableStateOf("omar") }
        val auth = FirebaseAuth.getInstance()

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Bienvenido, por favor inicia sesión")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de usuario") },
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.padding(16.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (username.isNotEmpty() && password.isNotEmpty()) {
                            // Iniciar sesión
                            auth.signInWithEmailAndPassword(username, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        navController.navigate(Routes.MapScreen.route)

                                    } else {
                                        // Error durante el inicio de sesión
                                        val errorMessage = "ERROR" +
                                            task.exception?.message ?: "Error al iniciar sesión"
                                        println(errorMessage)
                                    }
                                }
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Iniciar sesión")
                }
            }
        }
    }
