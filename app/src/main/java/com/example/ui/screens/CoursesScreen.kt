package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.CourseItem
import com.example.data.api.CreateLessonRequest
import com.example.data.api.QuizQuestion
import com.example.ui.components.NeonmanButton
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
    token: String?,
    courses: List<CourseItem>,
    isLoading: Boolean,
    onOpenCourseDetail: (String) -> Unit,
    onCreateCourseManual: (title: String, description: String, lessons: List<CreateLessonRequest>) -> Unit,
    onMenuClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    val context = LocalContext.current

    var showBottomSheet by remember { mutableStateOf(false) }
    var activeModalType by remember { mutableStateOf<ModalType?>(null) } // MCP, CONSTRUCTOR

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Courses",
                onMenuClick = onMenuClick,
                isDark = isDark
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    activeModalType = null
                    showBottomSheet = true
                },
                containerColor = if (isDark) NeonCyan else Color.White,
                contentColor = if (isDark) DarkBackground else SketchBorder,
                shape = CircleShape,
                modifier = Modifier.border(
                    width = if (isDark) 0.dp else 2.dp,
                    color = if (isDark) Color.Transparent else SketchBorder,
                    shape = CircleShape
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Course",
                    modifier = Modifier.size(28.dp)
                )
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
            if (isLoading && courses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (courses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = if (isDark) DarkTextSecondary else SketchTextSecondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No courses available yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) DarkTextPrimary else SketchTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap the '+' button below to create a new course",
                            fontSize = 13.sp,
                            color = if (isDark) DarkTextSecondary else SketchTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(courses) { course ->
                        CourseCardItem(
                            course = course,
                            isDark = isDark,
                            onClick = { onOpenCourseDetail(course.id) }
                        )
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = if (isDark) DarkElevatedSurface else Color.White
        ) {
            if (activeModalType == null) {
                // Main Bottom Sheet Options Menu (3 Options)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Add New Course",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Option 1: Neonman AI (Disabled)
                    Surface(
                        color = if (isDark) DarkCardPanel.copy(alpha = 0.5f) else Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Neonman AI",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Not connected yet",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = "Coming Soon",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Option 2: Via MCP
                    Surface(
                        color = if (isDark) DarkCardPanel else Color.White,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .border(
                                1.dp,
                                if (isDark) NeonCyan else SketchBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { activeModalType = ModalType.MCP }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Extension,
                                contentDescription = null,
                                tint = if (isDark) NeonCyan else SketchTextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Via MCP",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                )
                                Text(
                                    text = "Create using your personal MCP endpoint",
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

                    // Option 3: Constructor
                    Surface(
                        color = if (isDark) DarkCardPanel else Color.White,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .border(
                                1.dp,
                                if (isDark) NeonViolet else SketchBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { activeModalType = ModalType.CONSTRUCTOR }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = if (isDark) NeonViolet else SketchTextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Course Builder",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                )
                                Text(
                                    text = "Manually construct courses and lessons",
                                    fontSize = 12.sp,
                                    color = if (isDark) DarkTextSecondary else SketchTextSecondary
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = if (isDark) NeonViolet else SketchTextPrimary
                            )
                        }
                    }
                }
            } else if (activeModalType == ModalType.MCP) {
                // MCP View
                val mcpLink = "https://mvp-neonman-academy.vercel.app/${token ?: ""}/mcp"

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "MCP Integration Endpoint",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "You can automatically push courses from any AI assistant or MCP client using this endpoint:",
                        fontSize = 13.sp,
                        color = if (isDark) DarkTextSecondary else SketchTextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = if (isDark) DarkCardPanel else Color(0xFFF0F0F0),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (isDark) NeonCyan else SketchBorder, RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = mcpLink,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) NeonCyan else SketchTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("MCP Link", mcpLink)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = if (isDark) NeonCyan else SketchTextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    NeonmanButton(
                        text = "Close",
                        onClick = { showBottomSheet = false },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (activeModalType == ModalType.CONSTRUCTOR) {
                // Constructor Form
                CourseConstructorContent(
                    isDark = isDark,
                    onSave = { title, desc, lessons ->
                        onCreateCourseManual(title, desc, lessons)
                        showBottomSheet = false
                    },
                    onCancel = { showBottomSheet = false }
                )
            }
        }
    }
}

private enum class ModalType {
    MCP, CONSTRUCTOR
}

@Composable
private fun CourseCardItem(
    course: CourseItem,
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
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDark) DarkElevatedSurface else Color.White)
                    .border(1.dp, if (isDark) NeonCyan else SketchBorder, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = if (isDark) NeonCyan else SketchTextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkTextPrimary else SketchTextPrimary
                )
                if (!course.description.isNullOrEmpty()) {
                    Text(
                        text = course.description,
                        fontSize = 12.sp,
                        color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                        maxLines = 2,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                color = if (isDark) NeonCyan.copy(alpha = 0.15f) else Color(0xFFEEEEEE),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "${course.lessonCount ?: 0} lessons",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) NeonCyan else SketchTextPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CourseConstructorContent(
    isDark: Boolean,
    onSave: (title: String, description: String, lessons: List<CreateLessonRequest>) -> Unit,
    onCancel: () -> Unit
) {
    var courseTitle by remember { mutableStateOf("") }
    var courseDescription by remember { mutableStateOf("") }
    val lessonsList = remember { mutableStateListOf<CreateLessonRequest>() }

    var showAddLessonDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 520.dp)
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Course Builder",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) DarkTextPrimary else SketchTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = courseTitle,
            onValueChange = { courseTitle = it },
            label = { Text("Course Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = courseDescription,
            onValueChange = { courseDescription = it },
            label = { Text("Course Description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lessons (${lessonsList.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )
            TextButton(onClick = { showAddLessonDialog = true }) {
                Text("+ Add Lesson", color = if (isDark) NeonCyan else SketchTextPrimary)
            }
        }

        lessonsList.forEachIndexed { index, lesson ->
            Surface(
                color = if (isDark) DarkCardPanel else Color(0xFFF9F9F9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, if (isDark) DarkBorder else SketchBorder, RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}. ${lesson.title}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { lessonsList.removeAt(index) }) {
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

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel", color = Color.Gray)
            }

            NeonmanButton(
                text = "Save",
                onClick = {
                    if (courseTitle.isNotBlank()) {
                        onSave(courseTitle, courseDescription, lessonsList.toList())
                    }
                },
                enabled = courseTitle.isNotBlank(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showAddLessonDialog) {
        AddLessonDialog(
            isDark = isDark,
            onDismiss = { showAddLessonDialog = false },
            onAdd = { lesson ->
                lessonsList.add(lesson)
                showAddLessonDialog = false
            }
        )
    }
}

@Composable
private fun AddLessonDialog(
    isDark: Boolean,
    onDismiss: () -> Unit,
    onAdd: (CreateLessonRequest) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var questionText by remember { mutableStateOf("") }
    var opt1 by remember { mutableStateOf("") }
    var opt2 by remember { mutableStateOf("") }
    var opt3 by remember { mutableStateOf("") }
    var correctIndex by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Lesson", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Lesson Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Lesson Content / Text") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("Quiz Question (Optional):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    label = { Text("Question Text") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = opt1, onValueChange = { opt1 = it }, label = { Text("Option A") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = opt2, onValueChange = { opt2 = it }, label = { Text("Option B") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = opt3, onValueChange = { opt3 = it }, label = { Text("Option C") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val quizList = if (questionText.isNotBlank() && opt1.isNotBlank() && opt2.isNotBlank()) {
                        listOf(QuizQuestion(question = questionText, options = listOf(opt1, opt2, opt3).filter { it.isNotBlank() }, correctIndex = correctIndex))
                    } else null

                    onAdd(CreateLessonRequest(title = title, content = content, quiz = quizList))
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
