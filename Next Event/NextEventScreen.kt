package com.example.mobcomfinal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class Event(
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val details: String,
    val isImportant: Boolean = false
)

@Composable
fun NextEventScreen(navController: NavHostController) {
    val upcomingEvents = listOf(
        Event(
            title = "CICS General Assembly",
            date = "Dec 15, 2025",
            time = "9:00 AM - 12:00 PM",
            location = "CICS Function Hall",
            details = "Mandatory attendance for all CICS students.",
            isImportant = true
        ),
        Event(
            title = "Thesis Proposal Defense",
            date = "Dec 16, 2025",
            time = "1:00 PM",
            location = "Room 305",
            details = "For 4th Year Computer Science students.",
        ),
        Event(
            title = "Freshman Orientation",
            date = "Jan 5, 2026",
            time = "8:00 AM",
            location = "Auditorium",
            details = "Welcome event for new students.",
        )
    )

    val nextEvent = upcomingEvents.firstOrNull { it.isImportant } ?: upcomingEvents.first()
    val otherEvents = upcomingEvents.filter { it != nextEvent }

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
                .padding(top = 40.dp, bottom = 24.dp, start = 12.dp, end = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Upcoming Events",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                NextEventHeroCard(event = nextEvent)
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Other Scheduled Events",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A237E),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (otherEvents.isNotEmpty()) {
                items(otherEvents) { event ->
                    EventListItem(event = event)
                }
            } else {
                item {
                    Text(
                        text = "🎉 No further events currently scheduled!",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun NextEventHeroCard(event: Event) {
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
                        listOf(Color(0xFF4CAF50), Color(0xFF388E3C))
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(24.dp)
        ) {
            Text(
                text = "Next Event:",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = event.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Divider(Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha = 0.3f))

            EventDetailRow(
                icon = Icons.Default.CalendarMonth,
                label = event.date,
                color = Color.White
            )
            
            EventDetailRow(
                icon = Icons.Default.CalendarMonth,
                label = event.time,
                color = Color.White
            )

            EventDetailRow(
                icon = Icons.Default.LocationOn,
                label = event.location,
                color = Color.White
            )
        }
    }
}

@Composable
fun EventListItem(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
                Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color(0xFF3949AB),
                modifier = Modifier.size(36.dp).padding(end = 8.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = event.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${event.date} • ${event.time} at ${event.location}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = event.details,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun EventDetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = label, color = color, fontSize = 15.sp)
    }
}
