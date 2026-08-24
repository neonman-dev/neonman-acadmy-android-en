package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.ScheduleItem
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun ScheduleScreen(
    schedules: List<ScheduleItem>,
    isLoading: Boolean,
    onAddSchedule: (title: String, dayLabel: String, startTime: String, endTime: String, notes: String?) -> Unit,
    onDeleteSchedule: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Class Schedule",
                onBackClick = onBackClick,
                isDark = isDark
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = if (isDark) NeonCyan else Color.White,
                contentColor = if (isDark) DarkBackground else SketchBorder,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Schedule")
            }
        },
        containerColor = if (isDark) DarkBackground else SketchBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .sketchGridBackground(isDark)
        ) {
            if (isLoading && schedules.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (schedules.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No scheduled classes yet", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(schedules) { item ->
                        NeonmanCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = if (isDark) NeonCyan.copy(alpha = 0.15f) else Color(0xFFE0F7FA),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = item.dayLabel,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isDark) NeonCyan else SketchTextPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                    )
                                    Text(
                                        text = "${item.startTime} - ${item.endTime}",
                                        fontSize = 12.sp,
                                        color = if (isDark) DarkTextSecondary else SketchTextSecondary
                                    )
                                    if (!item.notes.isNullOrEmpty()) {
                                        Text(
                                            text = item.notes,
                                            fontSize = 11.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                                IconButton(onClick = { onDeleteSchedule(item.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = NeonDanger,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddScheduleDialog(
            isDark = isDark,
            onDismiss = { showAddDialog = false },
            onAdd = { title, day, start, end, notes ->
                onAddSchedule(title, day, start, end, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddScheduleDialog(
    isDark: Boolean,
    onDismiss: () -> Unit,
    onAdd: (title: String, dayLabel: String, startTime: String, endTime: String, notes: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var dayLabel by remember { mutableStateOf("Monday") }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("10:30") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Class Schedule", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Class Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = dayLabel,
                    onValueChange = { dayLabel = it },
                    label = { Text("Day (e.g., Monday)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start Time") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End Time") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title, dayLabel, startTime, endTime, notes.ifBlank { null })
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        containerColor = if (isDark) DarkElevatedSurface else Color.White
    )
}
