package com.example.mappproject

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@Composable
fun AddMarkerScreen(navController: NavController) {
    var markerName by remember { mutableStateOf("") }
    var markerImageUri by remember { mutableStateOf<String?>(null) }
    val auth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance()
    val currentUser = auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = markerName,
            onValueChange = { markerName = it },
            label = { Text("Nombre del marcador") },
            modifier = Modifier.padding(vertical = 8.dp)
        )
        // Botón para seleccionar imagen
        Button(
            onClick = {
                // Lanzar la actividad para seleccionar imagen
                selectImage.launch("image/*")
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Seleccionar imagen")
        }
        // Mostrar la imagen seleccionada
        markerImageUri?.let { uri ->
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_image_24),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .size(100.dp)
                    .padding(vertical = 8.dp)
            )
        }
        // Botón para guardar el marcador
        Button(
            onClick = {
                if (markerName.isNotBlank() && markerImageUri != null && currentUser != null) {
                    // Guardar la imagen en Firebase Storage
                    val storageRef = storage.reference
                    val imagesRef = storageRef.child("images/${currentUser.uid}/${markerName}")
                    val uploadTask = imagesRef.putFile(markerImageUri!!)
                    uploadTask.addOnSuccessListener {
                        // Guardar los datos del marcador en la base de datos
                        // Por simplicidad, aquí solo imprimimos los datos
                        println("Marcador guardado:")
                        println("Nombre: $markerName")
                        println("Imagen URI: $markerImageUri")
                    }.addOnFailureListener {
                        // Error al cargar la imagen
                        println("Error al cargar la imagen: ${it.message}")
                    }
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Guardar marcador")
        }
    }
}