package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.CourseDetail
import com.example.data.api.LessonItem
import com.example.data.api.QuizQuestion
import com.example.ui.components.NeonmanButton
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun CourseDetailScreen(
    courseDetailState: com.example.ui.viewmodels.CourseDetailState,
    onOpenLesson: (lessonIndex: Int) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Course Lessons",
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
            when (courseDetailState) {
                is com.example.ui.viewmodels.CourseDetailState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                    }
                }
                is com.example.ui.viewmodels.CourseDetailState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = courseDetailState.message,
                            color = NeonDanger,
                            fontSize = 14.sp
                        )
                    }
                }
                is com.example.ui.viewmodels.CourseDetailState.Success -> {
                    val course = courseDetailState.course
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                Text(
                                    text = course.title,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                )
                                if (!course.description.isNullOrEmpty()) {
                                    Text(
                                        text = course.description,
                                        fontSize = 13.sp,
                                        color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Lessons List (${course.lessons.size}):",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                )
                            }
                        }

                        if (course.lessons.isEmpty()) {
                            item {
                                Text(
                                    text = "No lessons available in this course yet.",
                                    fontSize = 13.sp,
                                    color = if (isDark) DarkTextSecondary else SketchTextSecondary
                                )
                            }
                        } else {
                            itemsIndexed(course.lessons) { index, lesson ->
                                LessonRowItem(
                                    index = index + 1,
                                    lesson = lesson,
                                    isDark = isDark,
                                    onClick = { onOpenLesson(index) }
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
private fun LessonRowItem(
    index: Int,
    lesson: LessonItem,
    isDark: Boolean,
    onClick: () -> Unit
) {
    NeonmanCard(
        isDark = isDark,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isDark) NeonCyan.copy(alpha = 0.15f) else Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$index",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) NeonCyan else SketchTextPrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                )
                if (!lesson.quiz.isNullOrEmpty()) {
                    Text(
                        text = "📝 Interactive quiz available",
                        fontSize = 11.sp,
                        color = if (isDark) NeonViolet else SketchTextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = if (isDark) NeonCyan else SketchTextPrimary
            )
        }
    }
}

@Composable
fun LessonDetailScreen(
    courseTitle: String,
    lesson: LessonItem,
    token: String?,
    onSubmitAttempt: (selectedIndex: Int, isCorrect: Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    val context = LocalContext.current

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = lesson.title,
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = lesson.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lesson Content
            NeonmanCard(
                isDark = isDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = lesson.content.ifBlank { "No lesson content available." },
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Interactive Quiz Section if available
            if (!lesson.quiz.isNullOrEmpty()) {
                Text(
                    text = "Interactive Quiz",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                lesson.quiz.forEachIndexed { qIndex, quizItem ->
                    InteractiveQuizCard(
                        quiz = quizItem,
                        qIndex = qIndex + 1,
                        isDark = isDark,
                        onSubmit = { selectedIdx, isCorrect ->
                            onSubmitAttempt(selectedIdx, isCorrect)
                            val msg = if (isCorrect) "Great job! Correct answer! 🎉" else "Incorrect answer, please try again! ❌"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun InteractiveQuizCard(
    quiz: QuizQuestion,
    qIndex: Int,
    isDark: Boolean,
    onSubmit: (selectedIndex: Int, isCorrect: Boolean) -> Unit
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    NeonmanCard(
        isDark = isDark,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Quiz,
                contentDescription = null,
                tint = if (isDark) NeonCyan else SketchTextPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$qIndex. ${quiz.question}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        quiz.options.forEachIndexed { index, option ->
            val isSelected = selectedOption == index
            val isCorrect = index == quiz.correctIndex

            val optionBg = when {
                isAnswerSubmitted && isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                isAnswerSubmitted && isSelected && !isCorrect -> NeonDanger.copy(alpha = 0.2f)
                isSelected -> if (isDark) NeonCyan.copy(alpha = 0.15f) else Color(0xFFE0F7FA)
                else -> if (isDark) DarkElevatedSurface else Color.White
            }

            val optionBorder = when {
                isAnswerSubmitted && isCorrect -> Color(0xFF4CAF50)
                isAnswerSubmitted && isSelected && !isCorrect -> NeonDanger
                isSelected -> if (isDark) NeonCyan else SketchBorder
                else -> if (isDark) DarkBorder else SketchBorder
            }

            Surface(
                color = optionBg,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, optionBorder, RoundedCornerShape(8.dp))
                    .clickable(enabled = !isAnswerSubmitted) {
                        selectedOption = index
                    }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { if (!isAnswerSubmitted) selectedOption = index },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = if (isDark) NeonCyan else SketchBorder
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = option,
                        fontSize = 14.sp,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (!isAnswerSubmitted) {
            NeonmanButton(
                text = "Submit Answer",
                onClick = {
                    if (selectedOption != null) {
                        isAnswerSubmitted = true
                        val correct = selectedOption == quiz.correctIndex
                        onSubmit(selectedOption!!, correct)
                    }
                },
                enabled = selectedOption != null,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            val isRight = selectedOption == quiz.correctIndex
            Text(
                text = if (isRight) "✅ Correct answer!" else "❌ Incorrect. Correct answer: ${quiz.options.getOrNull(quiz.correctIndex) ?: ""}",
                fontWeight = FontWeight.Bold,
                color = if (isRight) Color(0xFF4CAF50) else NeonDanger,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
