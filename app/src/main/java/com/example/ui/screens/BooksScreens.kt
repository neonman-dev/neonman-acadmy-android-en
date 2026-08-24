package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.BookDetail
import com.example.data.api.BookItem
import com.example.data.api.ChapterItem
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun BooksListScreen(
    books: List<BookItem>,
    isLoading: Boolean,
    onOpenBook: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Books & Library",
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
            if (isLoading && books.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (books.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No books found", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(books) { book ->
                        NeonmanCard(
                            isDark = isDark,
                            onClick = { onOpenBook(book.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isDark) NeonViolet.copy(alpha = 0.15f) else Color(0xFFF0F0F0))
                                        .border(1.dp, if (isDark) NeonViolet else SketchBorder, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = if (isDark) NeonViolet else SketchTextPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = book.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                    )
                                    if (!book.description.isNullOrEmpty()) {
                                        Text(
                                            text = book.description,
                                            fontSize = 12.sp,
                                            color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                                            maxLines = 1
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = if (isDark) NeonViolet else SketchTextPrimary
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
fun BookDetailScreen(
    bookDetail: BookDetail?,
    isLoading: Boolean,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    var selectedChapterIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = if (selectedChapterIndex != null) {
                    bookDetail?.chapters?.getOrNull(selectedChapterIndex!!)?.title ?: "Chapter"
                } else {
                    bookDetail?.title ?: "Book"
                },
                onBackClick = {
                    if (selectedChapterIndex != null) {
                        selectedChapterIndex = null
                    } else {
                        onBackClick()
                    }
                },
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
            if (isLoading && bookDetail == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (bookDetail == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Book information not found", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else if (selectedChapterIndex != null) {
                // Chapter Content View
                val chapter = bookDetail.chapters.getOrNull(selectedChapterIndex!!)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = chapter?.title ?: "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    NeonmanCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = chapter?.content ?: "No chapter content available",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = if (isDark) DarkTextPrimary else SketchTextPrimary
                        )
                    }
                }
            } else {
                // Chapter List View
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = bookDetail.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Chapters List (${bookDetail.chapters.size}):",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) DarkTextSecondary else SketchTextSecondary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    itemsIndexed(bookDetail.chapters) { idx, chapter ->
                        NeonmanCard(
                            isDark = isDark,
                            onClick = { selectedChapterIndex = idx },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${idx + 1}. ${chapter.title}",
                                    fontSize = 15.sp,
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
