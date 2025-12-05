package com.example.mobcomfinal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun HomeScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var userName by remember { mutableStateOf("Student") }
    var qrData by remember { mutableStateOf("") }
    val database = FirebaseDatabase
        .getInstance("https://cics-smartpass-default-rtdb.firebaseio.com")
        .reference.child("users")

    LaunchedEffect(true) {
        user?.uid?.let { uid ->
            database.child(uid).get().addOnSuccessListener { snapshot ->
                userName = snapshot.child("name").value?.toString() ?: "Student"
                qrData = uid
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1A237E), Color(0xFF303F9F))
                    )
                )
                .padding(vertical = 30.dp, horizontal = 24.dp)
        ) {
            Column {
                Text(
                    text = "Welcome, $userName!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Your CICS SmartPass — secure attendance using QR code.",
                    fontSize = 14.sp,
                    color = Color(0xFFD6DAFF)
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 24.dp, bottom = 50.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(26.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Your QR Code",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A237E)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (qrData.isNotEmpty()) {
                        QRCodeImage(
                            data = qrData,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Do not share this QR code with anyone.",
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FeatureCard(
                        title = "Next Event",
                        subtitle = "See what's coming",
                        onClick = { navController.navigate("next_event") },
                        gradientColors = listOf(Color(0xFF1A237E), Color(0xFF3949AB))
                    )
                    FeatureCard(
                        title = "My Profile",
                        subtitle = "Student details",
                        onClick = { navController.navigate("my_profile") },
                        gradientColors = listOf(Color(0xFF1A237E), Color(0xFF3949AB))
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FeatureCard(
                        title = "Excuse Letter",
                        subtitle = "Submit request",
                        onClick = { navController.navigate("excuse_letter") },
                        gradientColors = listOf(Color(0xFF1A237E), Color(0xFF3949AB))
                    )
                    FeatureCard(
                        title = "Logout",
                        subtitle = "Sign out safely",
                        onClick = {
                            auth.signOut()
                            navController.navigate("login") { popUpTo(0) }
                        },
                        gradientColors = listOf(Color(0xFF1A237E), Color(0xFF3949AB))
                    )
                }
            }
            Spacer(modifier = Modifier.height(35.dp))
            GradientCard(
                title = "🌍 Supporting SDG 9",
                subtitle = "\"Industry, Innovation, and Infrastructure\"",
                description = "CICS SmartPass uses digital QR systems to strengthen innovation inside CICS and promote efficient, secure, and modern attendance tracker.",
                gradientColors = listOf(Color(0xFF1A237E), Color(0xFF5E35B1), Color(0xFF3949AB))
            )
        }
    }
}
@Composable
fun FeatureCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    gradientColors: List<Color>
) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(gradientColors),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}
@Composable
fun GradientCard(
    title: String,
    subtitle: String,
    description: String,
    gradientColors: List<Color>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(gradientColors),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color(0xFFE8F5E9)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}
