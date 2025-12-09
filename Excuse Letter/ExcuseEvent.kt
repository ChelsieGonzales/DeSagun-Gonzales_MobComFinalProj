package com.example.mobcomfinal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import kotlinx.coroutines.launch

data class ExcuseEvent(val title: String, val date: String)

@Composable
fun ExcuseLetterScreen(navController: NavHostController) {
    val eventList = listOf(
        ExcuseEvent(title = "CICS General Assembly", date = "Dec 16, 2025"),
        ExcuseEvent(title = "Thesis Proposal Defense", date = "Dec 16, 2025"),
        ExcuseEvent(title = "None (Absence Orientation)", date = "Jan 5, 2026 (N/A)"),
    )

    var recipient by remember { mutableStateOf("CICS Dean's Office") }
    var subject by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var absenceDate by remember { mutableStateOf("Dec 15, 2025") }
    var attachmentStatus by remember { mutableStateOf("No file attached") }

    var selectedEvent by remember { mutableStateOf<ExcuseEvent?>(eventList.first()) }
    var isEventDropdownExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun submitExcuse() {
        if (subject.isBlank() || reason.isBlank() || selectedEvent == null || absenceDate.isBlank()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "⚠️ Please fill in all required fields.",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = "✅ Request Submitted for: ${selectedEvent!!.title}",
                duration = SnackbarDuration.Long
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ExcuseLetterTopBar(navController = navController)
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

            EventSelectorCard(
                eventList = eventList,
                selectedEvent = selectedEvent,
                isExpanded = isEventDropdownExpanded,
                onExpandChange = { isEventDropdownExpanded = it },
                onEventSelect = {
                    selectedEvent = it
                    absenceDate = if (it.date != "N/A") it.date.split(" ").first() else "Dec 15, 2025" // Keep original date if N/A
                    isEventDropdownExpanded = false
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            FormCard {
                LabeledTextDisplay(
                    label = "To:",
                    value = recipient,
                    icon = Icons.Default.Business
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject of Request*") },
                    leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject of Request*") },
                    leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = absenceDate,
                    onValueChange = { absenceDate = it },
                    label = { Text("Date(s) of Absence*") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.clickable { /* TODO: Date Picker */ }) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Detailed Reason for Absence*") },
                    leadingIcon = { Icon(Icons.Default.EditNote, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp),
                    maxLines = 10
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        attachmentStatus = "File attached: MedicalCert.pdf"
                        snackbarHostState.showSnackbar("File picker functionality needed here.")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF512DA8)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.UploadFile, contentDescription = "Attach File")
                Spacer(Modifier.width(8.dp))
                Text("Upload Supporting Document", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Attachment Status: $attachmentStatus",
                fontSize = 12.sp,
                color = if (attachmentStatus.contains("No file")) Color.Red else Color.Green
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = ::submitExcuse,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(listOf(Color(0xFF673AB7), Color(0xFF512DA8))),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("SUBMIT REQUEST", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EventSelectorCard(
    eventList: List<ExcuseEvent>,
    selectedEvent: ExcuseEvent?,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onEventSelect: (ExcuseEvent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Event/Class Missed*",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandChange(!isExpanded) }
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Display Selected Event
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF673AB7),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "${selectedEvent?.title ?: "Select Event..."} (${selectedEvent?.date ?: ""})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                    // Dropdown Arrow
                    Icon(
                        if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Expand",
                        tint = Color.Gray
                    )
                }

                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { onExpandChange(false) },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    eventList.forEach { event ->
                        DropdownMenuItem(
                            text = { Text("${event.title} (${event.date})") },
                            onClick = { onEventSelect(event) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExcuseLetterTopBar(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF303F9F)))
            )
            .padding(top = 40.dp, bottom = 24.dp, start = 12.dp, end = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Submit Excuse Letter",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun FormCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

@Composable
fun LabeledTextDisplay(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        }
    }
}
