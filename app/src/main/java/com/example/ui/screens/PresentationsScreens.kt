package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.PresentationDetail
import com.example.data.api.PresentationItem
import com.example.ui.components.NeonmanButton
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun PresentationsListScreen(
    presentations: List<PresentationItem>,
    isLoading: Boolean,
    onOpenPresentation: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Presentations",
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
            if (isLoading && presentations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (presentations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No presentations found", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(presentations) { item ->
                        NeonmanCard(
                            isDark = isDark,
                            onClick = { onOpenPresentation(item.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isDark) NeonCyan.copy(alpha = 0.15f) else Color(0xFFF0F0F0))
                                        .border(1.dp, if (isDark) NeonCyan else SketchBorder, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Slideshow,
                                        contentDescription = null,
                                        tint = if (isDark) NeonCyan else SketchTextPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = item.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
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
fun PresentationViewerScreen(
    presentationDetail: PresentationDetail?,
    isLoading: Boolean,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    var currentSlideIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = presentationDetail?.title ?: "Presentation",
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
            if (isLoading && presentationDetail == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (presentationDetail == null || presentationDetail.slides.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No slides found", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else {
                val slide = presentationDetail.slides[currentSlideIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Slide ${currentSlideIndex + 1} / ${presentationDetail.slides.size}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) NeonCyan else SketchTextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Slide Card Container
                    NeonmanCard(
                        isDark = isDark,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = slide.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        slide.bullets.forEach { bullet ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "• ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (isDark) NeonCyan else SketchTextPrimary
                                )
                                Text(
                                    text = bullet,
                                    fontSize = 15.sp,
                                    color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { if (currentSlideIndex > 0) currentSlideIndex-- },
                            enabled = currentSlideIndex > 0,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("<- Previous")
                        }

                        NeonmanButton(
                            text = "Next ->",
                            onClick = { if (currentSlideIndex + 1 < presentationDetail.slides.size) currentSlideIndex++ },
                            enabled = currentSlideIndex + 1 < presentationDetail.slides.size,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
