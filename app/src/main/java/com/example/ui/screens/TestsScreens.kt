package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.TestDetail
import com.example.data.api.TestItem
import com.example.ui.components.NeonmanButton
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun TestsListScreen(
    tests: List<TestItem>,
    isLoading: Boolean,
    onOpenTest: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Self-Study Tests",
                onBackClick = onBackClick,
                isDark = isDark
            )
        },
        containerColor = if (isDark) DarkBackground else SketchBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .sketchGridBackground(isDark)
        ) {
            if (isLoading && tests.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (tests.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tests available", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tests) { test ->
                        NeonmanCard(
                            isDark = isDark,
                            onClick = { onOpenTest(test.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .border(1.dp, if (isDark) NeonCyan else SketchBorder, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Quiz,
                                        contentDescription = null,
                                        tint = if (isDark) NeonCyan else SketchTextPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = test.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                    )
                                    Text(
                                        text = "${test.questionCount ?: 0} questions",
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
                }
            }
        }
    }
}

@Composable
fun TestRunnerScreen(
    testDetail: TestDetail?,
    isLoading: Boolean,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    val context = LocalContext.current

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var isTestFinished by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = testDetail?.title ?: "Test Quiz",
                onBackClick = onBackClick,
                isDark = isDark
            )
        },
        containerColor = if (isDark) DarkBackground else SketchBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .sketchGridBackground(isDark)
        ) {
            if (isLoading && testDetail == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (testDetail == null || testDetail.questions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No test questions available", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else if (isTestFinished) {
                // Summary Screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Test Completed! 🎉",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "You answered $score out of ${testDetail.questions.size} questions correctly.",
                        fontSize = 16.sp,
                        color = if (isDark) DarkTextSecondary else SketchTextSecondary
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    NeonmanButton(
                        text = "Restart Test",
                        onClick = {
                            currentQuestionIndex = 0
                            score = 0
                            isTestFinished = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onBackClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Exit")
                    }
                }
            } else {
                // Question Runner View
                val question = testDetail.questions[currentQuestionIndex]
                var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Question ${currentQuestionIndex + 1} / ${testDetail.questions.size}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) NeonCyan else SketchTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NeonmanCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = question.question,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) DarkTextPrimary else SketchTextPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        question.options.forEachIndexed { optIdx, optText ->
                            val isSelected = selectedOptionIndex == optIdx

                            Surface(
                                color = if (isSelected) {
                                    if (isDark) NeonCyan.copy(alpha = 0.2f) else Color(0xFFE0F7FA)
                                } else {
                                    if (isDark) DarkElevatedSurface else Color.White
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) (if (isDark) NeonCyan else SketchBorder) else (if (isDark) DarkBorder else SketchBorder),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedOptionIndex = optIdx }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedOptionIndex = optIdx },
                                        colors = RadioButtonDefaults.colors(selectedColor = if (isDark) NeonCyan else SketchBorder)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = optText,
                                        fontSize = 14.sp,
                                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    NeonmanButton(
                        text = if (currentQuestionIndex + 1 == testDetail.questions.size) "Finish" else "Next ->",
                        onClick = {
                            if (selectedOptionIndex == question.correctIndex) {
                                score++
                            }
                            if (currentQuestionIndex + 1 < testDetail.questions.size) {
                                currentQuestionIndex++
                                selectedOptionIndex = null
                            } else {
                                isTestFinished = true
                            }
                        },
                        enabled = selectedOptionIndex != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
