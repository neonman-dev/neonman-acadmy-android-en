package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.SavedItem
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun SavedScreen(
    savedItems: List<SavedItem>,
    isLoading: Boolean,
    onDeleteSaved: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Saved Materials",
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
            if (isLoading && savedItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (savedItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No saved materials yet", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(savedItems) { item ->
                        NeonmanCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = if (isDark) NeonCyan else SketchTextPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${item.itemType.uppercase()}: ${item.itemId}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                    )
                                }
                                IconButton(onClick = { onDeleteSaved(item.id) }) {
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
}
