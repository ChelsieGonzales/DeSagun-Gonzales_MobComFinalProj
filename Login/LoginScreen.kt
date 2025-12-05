package com.example.mobcomfinal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val gradientColors = listOf(
        Color(0xFF1A237E),
        Color(0xFF2962FF)
    )
    val backgroundBrush = Brush.verticalGradient(colors = gradientColors)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
        ) {
            Text(
                text = "Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Log in to view your attendance QR",
                fontSize = 15.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(top = 4.dp, bottom = 40.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    error = task.exception?.message ?: "Login failed"
                                }
                            }
                    } else {
                        error = "Please fill all fields"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)) // Darker blue for button
            ) {
                Text(text = "Login", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.navigate("signup") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            ) {
                Text("Don't have an account? Sign Up", color = Color.LightGray)
            }
            LaunchedEffect(error) {
                if (error.isNotEmpty()) {
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    error = ""
                }
            }
        }
    }
}
