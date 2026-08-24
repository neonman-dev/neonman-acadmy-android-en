package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

data class ExtraFeatureItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun ExtrasScreen(
    onNavigateFeature: (String) -> Unit,
    onMenuClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    val context = LocalContext.current

    val mainFeatures = listOf(
        ExtraFeatureItem("saved", "Saved", "Bookmarked materials", Icons.Default.Bookmark, NeonCyan),
        ExtraFeatureItem("books", "Books", "Manuals and digital library", Icons.Default.MenuBook, NeonViolet),
        ExtraFeatureItem("tests", "Tests", "Quizzes and skill tests", Icons.Default.Quiz, Color(0xFFFFB74D)),
        ExtraFeatureItem("games", "Games", "Flashcards & Matching", Icons.Default.SportsEsports, Color(0xFF81C784)),
        ExtraFeatureItem("presentations", "Presentations", "Slide-based interactive lessons", Icons.Default.Slideshow, Color(0xFFBA68C8)),
        ExtraFeatureItem("schedule", "Schedule", "Daily classes & time table", Icons.Default.CalendarMonth, Color(0xFF4FC3F7))
    )

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Extra Features",
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

            Text(
                text = "Interactive Sections",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 2 Column Grid Layout
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in mainFeatures.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeatureGridCard(
                            item = mainFeatures[i],
                            isDark = isDark,
                            onClick = { onNavigateFeature(mainFeatures[i].id) },
                            modifier = Modifier.weight(1f)
                        )
                        if (i + 1 < mainFeatures.size) {
                            FeatureGridCard(
                                item = mainFeatures[i + 1],
                                isDark = isDark,
                                onClick = { onNavigateFeature(mainFeatures[i + 1].id) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Section: Teacher Tools
            Text(
                text = "Teacher Tools & Add-ons",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // AI Whiteboard
            NeonmanCard(
                isDark = isDark,
                onClick = { onNavigateFeature("board") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDark) NeonCyan.copy(alpha = 0.15f) else Color(0xFFE0F7FA))
                            .border(1.dp, if (isDark) NeonCyan else SketchBorder, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Draw,
                            contentDescription = null,
                            tint = if (isDark) NeonCyan else SketchTextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Whiteboard",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) DarkTextPrimary else SketchTextPrimary
                        )
                        Text(
                            text = "Interactive drawing and board canvas",
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

            // AI Chat (Coming Soon)
            NeonmanCard(
                isDark = isDark,
                onClick = {
                    Toast.makeText(context, "AI Chat service will be available soon! 🚀", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDark) DarkElevatedSurface else Color(0xFFF5F5F5))
                            .border(1.dp, Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Chat",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "Q&A and smart AI learning assistant",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Surface(
                        color = Color.Gray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Coming Soon",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureGridCard(
    item: ExtraFeatureItem,
    isDark: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NeonmanCard(
        isDark = isDark,
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDark) item.color.copy(alpha = 0.15f) else Color(0xFFF0F0F0))
                    .border(
                        1.dp,
                        if (isDark) item.color else SketchBorder,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (isDark) item.color else SketchTextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )

            Text(
                text = item.description,
                fontSize = 11.sp,
                color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                maxLines = 2,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
