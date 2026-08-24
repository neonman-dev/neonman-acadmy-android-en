package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.NeonmanButton
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    token: String?,
    onNavigateNext: (Boolean) -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark

    LaunchedEffect(Unit) {
        delay(1800)
        onNavigateNext(!token.isNullOrEmpty())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkBackground else SketchBackground)
            .sketchGridBackground(isDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(if (isDark) DarkCardPanel else Color.White)
                    .border(
                        3.dp,
                        if (isDark) NeonCyan else SketchBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_neonman_logo),
                    contentDescription = "Neonman Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Neonman Academy",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "MCP Learning & AI Platform",
                fontSize = 14.sp,
                color = if (isDark) DarkTextSecondary else SketchTextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                color = if (isDark) NeonCyan else SketchBorder,
                modifier = Modifier.size(28.dp),
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
fun LoginScreen(
    onDemoLogin: () -> Unit,
    onTelegramLogin: (String) -> Unit,
    onGoogleLoginRequested: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    val isDark = LocalExtraThemeColors.current.isDark
    var showTelegramDialog by remember { mutableStateOf(false) }
    var telegramInitData by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkBackground else SketchBackground)
            .sketchGridBackground(isDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo Header
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(if (isDark) DarkCardPanel else Color.White)
                    .border(2.dp, if (isDark) NeonCyan else SketchBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_neonman_logo),
                    contentDescription = "Neonman Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Welcome!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )

            Text(
                text = "Sign in to the Neonman Academy platform",
                fontSize = 14.sp,
                color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            if (!errorMessage.isNullOrEmpty()) {
                Surface(
                    color = NeonDanger.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .border(1.dp, NeonDanger, RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = errorMessage,
                        color = NeonDanger,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Google and Telegram side-by-side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Google Button
                OutlinedButton(
                    onClick = onGoogleLoginRequested,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isDark) DarkCardPanel else Color.White,
                        contentColor = if (isDark) DarkTextPrimary else SketchTextPrimary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = if (isDark) 1.dp else 2.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (isDark) DarkBorder else SketchBorder
                        )
                    )
                ) {
                    Text(
                        text = "G  Google",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                // Telegram Button
                OutlinedButton(
                    onClick = { showTelegramDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isDark) DarkCardPanel else Color.White,
                        contentColor = if (isDark) NeonCyan else SketchTextPrimary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = if (isDark) 1.dp else 2.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (isDark) NeonCyan else SketchBorder
                        )
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Telegram",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Full width Demo Login button
            NeonmanButton(
                text = if (isLoading) "Signing in..." else "Sign In with Demo Account",
                onClick = onDemoLogin,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                testTagStr = "demo_login_button"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Demo account provides instant access to all courses, tests, games, and tools",
                fontSize = 12.sp,
                color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }

    if (showTelegramDialog) {
        AlertDialog(
            onDismissRequest = { showTelegramDialog = false },
            title = {
                Text(
                    text = "Sign in via Telegram",
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter Telegram WebApp initData string:",
                        fontSize = 13.sp,
                        color = if (isDark) DarkTextSecondary else SketchTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = telegramInitData,
                        onValueChange = { telegramInitData = it },
                        placeholder = { Text("query_id=...&user=...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTelegramDialog = false
                        if (telegramInitData.isNotBlank()) {
                            onTelegramLogin(telegramInitData)
                        }
                    }
                ) {
                    Text("Sign In", color = if (isDark) NeonCyan else SketchTextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTelegramDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = if (isDark) DarkElevatedSurface else Color.White
        )
    }
}

@Composable
fun RegisterScreen(
    token: String,
    onComplete: (firstName: String, lastName: String, birthDate: String) -> Unit,
    isLoading: Boolean
) {
    val isDark = LocalExtraThemeColors.current.isDark

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var privacyAccepted by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkBackground else SketchBackground)
            .sketchGridBackground(isDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Complete Profile",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )

            Text(
                text = "Please fill in your details to continue",
                fontSize = 14.sp,
                color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            if (validationError.isNotEmpty()) {
                Text(
                    text = validationError,
                    color = NeonDanger,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // First name (mandatory)
            Text(
                text = "First Name *",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = { Text("Enter your first name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Last name (optional)
            Text(
                text = "Last Name (Optional)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = { Text("Enter your last name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Birth date (day/month/year - 3 inputs)
            Text(
                text = "Date of Birth (Day/Month/Year)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = day,
                    onValueChange = { if (it.length <= 2) day = it },
                    placeholder = { Text("DD") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = month,
                    onValueChange = { if (it.length <= 2) month = it },
                    placeholder = { Text("MM") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = year,
                    onValueChange = { if (it.length <= 4) year = it },
                    placeholder = { Text("YYYY") },
                    modifier = Modifier.weight(1.2f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Privacy agreement checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = privacyAccepted,
                    onCheckedChange = { privacyAccepted = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = if (isDark) NeonCyan else SketchBorder
                    )
                )
                Text(
                    text = "I agree to the terms of service & privacy policy",
                    fontSize = 13.sp,
                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            NeonmanButton(
                text = if (isLoading) "Saving..." else "Continue ->",
                onClick = {
                    if (firstName.isBlank()) {
                        validationError = "Please enter your first name!"
                    } else if (!privacyAccepted) {
                        validationError = "Please accept the terms of service!"
                    } else {
                        validationError = ""
                        val birthDateStr = if (day.isNotBlank() && month.isNotBlank() && year.isNotBlank()) {
                            "$day/$month/$year"
                        } else ""
                        onComplete(firstName, lastName, birthDateStr)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                testTagStr = "complete_registration_button"
            )
        }
    }
}
