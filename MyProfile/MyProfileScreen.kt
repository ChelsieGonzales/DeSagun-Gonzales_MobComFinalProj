package com.example.mobcomfinal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun MyProfileScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val database = FirebaseDatabase
        .getInstance("https://cics-smartpass-default-rtdb.firebaseio.com")
        .reference.child("users")

    var name by remember { mutableStateOf("Loading...") }
    var studentNumber by remember { mutableStateOf("Loading...") }
    val email = user?.email ?: "N/A"
    var contactNumber by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("Loading...") }
    var yearLevel by remember { mutableStateOf("Loading...") }

    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(user?.uid) {
        user?.uid?.let { uid ->
            database.child(uid).get().addOnSuccessListener { snapshot ->
                name = snapshot.child("name").value?.toString() ?: "Student Name"
                studentNumber = snapshot.child("studentNumber").value?.toString() ?: "N/A"
                contactNumber = snapshot.child("contactNumber").value?.toString() ?: ""
                course = snapshot.child("course").value?.toString() ?: "N/A"
                yearLevel = snapshot.child("yearLevel").value?.toString() ?: "N/A"
            }.addOnFailureListener {
            }
        }
    }

    fun saveProfile() {
        user?.uid?.let { uid ->
            val updates = mapOf(
                "name" to name,
                "contactNumber" to contactNumber,
            )
            database.child(uid).updateChildren(updates).addOnSuccessListener {
                isEditing = false
            }.addOnFailureListener {
            }
        }
    }

    Scaffold(
        topBar = {
            ProfileTopBar(
                navController = navController,
                isEditing = isEditing,
                onEditClick = { isEditing = !isEditing },
                onSaveClick = { saveProfile() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6FA))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
          
            ProfileHeroCard(
                name = name,
                studentNumber = studentNumber
            )

            Spacer(modifier = Modifier.height(30.dp))
            
            Text(
                text = "Account & Academic Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A237E),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            EditableDetailField(
                icon = Icons.Default.Person,
                title = "Full Name",
                value = name,
                onValueChange = { name = it },
                isEditing = isEditing
            )
            Spacer(modifier = Modifier.height(12.dp))

            DetailItemCard(
                icon = Icons.Default.Email,
                title = "Email Address (Non-Editable)",
                subtitle = email,
                iconColor = Color(0xFFC62828)
            )
            Spacer(modifier = Modifier.height(12.dp))

            EditableDetailField(
                icon = Icons.Default.Phone,
                title = "Contact Number",
                value = contactNumber,
                onValueChange = { contactNumber = it },
                isEditing = isEditing
            )
            Spacer(modifier = Modifier.height(12.dp))

            DetailItemCard(
                icon = Icons.Default.AssignmentInd,
                title = "Student ID",
                subtitle = studentNumber,
                iconColor = Color(0xFF303F9F)
            )
            Spacer(modifier = Modifier.height(12.dp))
=
            DetailItemCard(
                icon = Icons.Default.School,
                title = "Course & Year Level",
                subtitle = "$course ($yearLevel)",
                iconColor = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(30.dp))
            
            Text(
                text = if (isEditing) "Click SAVE to apply changes." else "Click EDIT to update your contact details.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ProfileTopBar(
    navController: NavHostController,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF303F9F)))
            )
            .padding(top = 40.dp, bottom = 16.dp, start = 12.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "My Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            if (isEditing) {
                Button(
                    onClick = onSaveClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = "Save", Modifier.size(20.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("SAVE")
                }
            } else {
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3949AB)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", Modifier.size(20.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("EDIT")
                }
            }
        }
    }
}

@Composable
fun EditableDetailField(
    icon: ImageVector,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF3949AB),
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                if (isEditing) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = Color(0xFF3949AB)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    )
                } else {
                    Text(
                        text = value,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A237E)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileHeroCard(name: String, studentNumber: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFF1A237E), Color(0xFF3949AB))
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(vertical = 32.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile Picture",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Student ID: $studentNumber",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun DetailItemCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color = Color(0xFF3949AB)
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = subtitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A237E)
                )
            }
        }
    }
}
