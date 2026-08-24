package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.UserProfile
import com.example.ui.components.NeonmanButton
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    userProfile: UserProfile?,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onNavigateAccount: () -> Unit,
    onNavigateExtras: () -> Unit,
    onLogout: () -> Unit,
    onMenuClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    val context = LocalContext.current

    var showHelpDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Settings & Profile",
                onMenuClick = onMenuClick,
                isDark = isDark
            )
        },
        containerColor = if (isDark) DarkBackground else SketchBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .sketchGridBackground(isDark)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // User Info Header Card
            NeonmanCard(
                isDark = isDark,
                onClick = onNavigateAccount,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .border(2.dp, if (isDark) NeonCyan else SketchBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (userProfile?.firstName?.take(1) ?: "N").uppercase(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) NeonCyan else SketchTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${userProfile?.firstName ?: "Neonman"} ${userProfile?.lastName ?: "User"}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) DarkTextPrimary else SketchTextPrimary
                        )
                        Text(
                            text = if (!userProfile?.birthDate.isNullOrEmpty()) "Date of birth: ${userProfile?.birthDate}" else "Manage account settings",
                            fontSize = 12.sp,
                            color = if (isDark) DarkTextSecondary else SketchTextSecondary
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = if (isDark) NeonCyan else SketchTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Settings Section
            Text(
                text = "Preferences & Menu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Menu Items
            SettingsMenuItem(
                icon = Icons.Default.Person,
                title = "Account",
                subtitle = "Edit personal information",
                isDark = isDark,
                onClick = onNavigateAccount
            )

            SettingsMenuItem(
                icon = Icons.Default.Apps,
                title = "Extra Features",
                subtitle = "Tests, games, books and whiteboard",
                isDark = isDark,
                onClick = onNavigateExtras
            )

            SettingsMenuItem(
                icon = Icons.Default.SmartToy,
                title = "AI Chat",
                subtitle = "Coming soon",
                isDark = isDark,
                onClick = {
                    Toast.makeText(context, "AI Chat will be available soon!", Toast.LENGTH_SHORT).show()
                }
            )

            SettingsMenuItem(
                icon = Icons.Default.Send,
                title = "Telegram Bot",
                subtitle = "Open official Telegram bot",
                isDark = isDark,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/neonman_academy_bot"))
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not open Telegram", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            // Theme Switcher Item
            NeonmanCard(
                isDark = isDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = if (isDark) NeonCyan else SketchTextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Dark Mode (Neon)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) DarkTextPrimary else SketchTextPrimary
                        )
                        Text(
                            text = if (isDarkMode) "Electro Neon Theme" else "Hand-drawn Sketch Theme",
                            fontSize = 12.sp,
                            color = if (isDark) DarkTextSecondary else SketchTextSecondary
                        )
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onToggleDarkMode,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonCyan,
                            checkedTrackColor = DarkElevatedSurface,
                            uncheckedThumbColor = SketchBorder,
                            uncheckedTrackColor = Color.White
                        )
                    )
                }
            }

            SettingsMenuItem(
                icon = Icons.Default.HelpOutline,
                title = "User Guide & Help",
                subtitle = "Learn more about app capabilities",
                isDark = isDark,
                onClick = { showHelpDialog = true }
            )

            SettingsMenuItem(
                icon = Icons.Default.Code,
                title = "Developer",
                subtitle = "NeonmanDev Team",
                isDark = isDark,
                onClick = {
                    Toast.makeText(context, "Author: NeonmanDev ⚡", Toast.LENGTH_SHORT).show()
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Logout Button
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonDanger),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = if (isDark) 1.dp else 2.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(NeonDanger)
                )
            ) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Log Out", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("About Neonman Academy", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "• MCP Platform — Education platform integrated with external AI & MCP servers.\n" +
                                "• Courses & Builder — Includes lessons, rich media, and interactive quizzes.\n" +
                                "• Dual Themes — Neon (Dark) and Hand-drawn Sketch (Light) styling.\n" +
                                "• AI Whiteboard — Interactive drawing, sketching, and teaching tools.",
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Got It")
                }
            },
            containerColor = if (isDark) DarkElevatedSurface else Color.White
        )
    }
}

@Composable
private fun SettingsMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isDark: Boolean,
    onClick: () -> Unit
) {
    NeonmanCard(
        isDark = isDark,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isDark) NeonCyan else SketchTextPrimary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = if (isDark) DarkTextSecondary else SketchTextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (isDark) NeonCyan else SketchTextPrimary
            )
        }
    }
}

@Composable
fun AccountEditScreen(
    userProfile: UserProfile?,
    onSaveProfile: (firstName: String, lastName: String, birthDate: String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    val context = LocalContext.current

    var firstName by remember(userProfile) { mutableStateOf(userProfile?.firstName ?: "") }
    var lastName by remember(userProfile) { mutableStateOf(userProfile?.lastName ?: "") }
    var birthDate by remember(userProfile) { mutableStateOf(userProfile?.birthDate ?: "") }

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Account Details",
                onBackClick = onBackClick,
                isDark = isDark
            )
        },
        containerColor = if (isDark) DarkBackground else SketchBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .sketchGridBackground(isDark)
                .padding(20.dp)
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // First Name Edit
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                trailingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Last Name Edit
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                trailingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Birth Date Edit
            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Date of Birth (DD/MM/YYYY)") },
                trailingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            NeonmanButton(
                text = "Save Changes",
                onClick = {
                    if (firstName.isNotBlank()) {
                        onSaveProfile(firstName, lastName, birthDate)
                        Toast.makeText(context, "Profile saved! ✅", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                },
                enabled = firstName.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
