package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.CourseItem
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    latestCourse: CourseItem?,
    userName: String?,
    onOpenCourseDetail: (String) -> Unit,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Neonman Academy",
                onMenuClick = onMenuClick,
                onSearchClick = onSearchClick,
                onMoreClick = onMoreClick,
                isDark = isDark
            )
        },
        containerColor = if (isDark) DarkBackground else SketchBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .sketchGridBackground(isDark)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Welcome Header
            item {
                Column {
                    Text(
                        text = "Hello, ${userName ?: "Student"}! 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                    )
                    Text(
                        text = "What would you like to learn today?",
                        fontSize = 13.sp,
                        color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Growth Section (Growth Graph)
            item {
                NeonmanCard(
                    isDark = isDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = if (isDark) NeonCyan else SketchTextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Learning Dynamics",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) DarkTextPrimary else SketchTextPrimary
                            )
                        }
                        Text(
                            text = "+28% this week",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) NeonCyan else SketchTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Line Graph Canvas
                    GrowthGraphCanvas(isDark = isDark)
                }
            }

            // Continue Learning Course Card
            item {
                Column {
                    Text(
                        text = "Continue Learning",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    if (latestCourse != null) {
                        NeonmanCard(
                            isDark = isDark,
                            onClick = { onOpenCourseDetail(latestCourse.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isDark) DarkElevatedSurface else Color.White)
                                        .border(1.dp, if (isDark) NeonCyan else SketchBorder, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Book,
                                        contentDescription = null,
                                        tint = if (isDark) NeonCyan else SketchTextPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = latestCourse.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                    )
                                    Text(
                                        text = latestCourse.description ?: "${latestCourse.lessonCount ?: 0} lessons available",
                                        fontSize = 12.sp,
                                        color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                                        maxLines = 1
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isDark) NeonCyan else SketchBorder),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Start",
                                        tint = if (isDark) DarkBackground else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        NeonmanCard(
                            isDark = isDark,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No courses in progress yet. Explore courses in the Courses tab.",
                                fontSize = 13.sp,
                                color = if (isDark) DarkTextSecondary else SketchTextSecondary
                            )
                        }
                    }
                }
            }

            // Notifications Section
            item {
                Column {
                    Text(
                        text = "Notifications",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    NeonmanCard(
                        isDark = isDark,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = if (isDark) DarkTextSecondary else SketchTextSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "No new notifications",
                                fontSize = 13.sp,
                                color = if (isDark) DarkTextSecondary else SketchTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GrowthGraphCanvas(isDark: Boolean) {
    val lineColor = if (isDark) NeonCyan else SketchBorder
    val gridLineColor = if (isDark) Color(0xFF232A4D) else Color(0xFFE0E0E0)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Score (%)", fontSize = 11.sp, color = if (isDark) DarkTextSecondary else SketchTextSecondary)
            Text("Days", fontSize = 11.sp, color = if (isDark) DarkTextSecondary else SketchTextSecondary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            val w = size.width
            val h = size.height

            // Horizontal Y grid lines (0%, 50%, 100%)
            val y1 = h * 0.1f
            val y2 = h * 0.5f
            val y3 = h * 0.9f

            drawLine(gridLineColor, Offset(0f, y1), Offset(w, y1), strokeWidth = 1f)
            drawLine(gridLineColor, Offset(0f, y2), Offset(w, y2), strokeWidth = 1f)
            drawLine(gridLineColor, Offset(0f, y3), Offset(w, y3), strokeWidth = 1f)

            // Line chart points: (Mon, Tue, Wed, Thu, Fri, Sat, Sun)
            val points = listOf(
                Offset(w * 0.05f, h * 0.85f),
                Offset(w * 0.20f, h * 0.70f),
                Offset(w * 0.35f, h * 0.75f),
                Offset(w * 0.50f, h * 0.45f),
                Offset(w * 0.65f, h * 0.50f),
                Offset(w * 0.80f, h * 0.25f),
                Offset(w * 0.95f, h * 0.15f)
            )

            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    lineTo(points[i].x, points[i].y)
                }
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 3.dp.toPx())
            )

            // Draw dot highlights
            points.forEach { point ->
                drawCircle(
                    color = lineColor,
                    radius = 4.dp.toPx(),
                    center = point
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                Text(day, fontSize = 11.sp, color = if (isDark) DarkTextSecondary else SketchTextSecondary)
            }
        }
    }
}
