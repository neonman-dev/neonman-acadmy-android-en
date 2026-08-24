package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.FlashcardItem
import com.example.data.api.GameDetail
import com.example.data.api.GameItem
import com.example.data.api.MatchingPair
import com.example.ui.components.NeonmanButton
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun GamesListScreen(
    games: List<GameItem>,
    isLoading: Boolean,
    onOpenGame: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "Games & Exercises",
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
            if (isLoading && games.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (games.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No games available", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(games) { game ->
                        NeonmanCard(
                            isDark = isDark,
                            onClick = { onOpenGame(game.id) },
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
                                        imageVector = Icons.Default.SportsEsports,
                                        contentDescription = null,
                                        tint = if (isDark) NeonCyan else SketchTextPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = game.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                    )
                                    Text(
                                        text = if (game.type == "flashcards") "Flashcards" else "Matching Pairs",
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
fun GamePlayScreen(
    gameDetail: GameDetail?,
    isLoading: Boolean,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = gameDetail?.title ?: "Game",
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
            if (isLoading && gameDetail == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (gameDetail == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Game not found", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else if (gameDetail.type == "flashcards") {
                FlashcardGameContent(
                    items = gameDetail.data?.items ?: emptyList(),
                    isDark = isDark
                )
            } else {
                MatchingGameContent(
                    pairs = gameDetail.data?.pairs ?: emptyList(),
                    isDark = isDark
                )
            }
        }
    }
}

@Composable
private fun FlashcardGameContent(
    items: List<FlashcardItem>,
    isDark: Boolean
) {
    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(400),
        label = "flip"
    )

    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No card data available", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
        }
        return
    }

    val currentCard = items[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Card ${currentIndex + 1} / ${items.size}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) NeonCyan else SketchTextPrimary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Flip Card Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12 * density
                }
                .clip(RoundedCornerShape(16.dp))
                .background(if (isDark) DarkCardPanel else Color.White)
                .border(2.dp, if (isDark) NeonCyan else SketchBorder, RoundedCornerShape(16.dp))
                .clickable { isFlipped = !isFlipped }
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "QUESTION / TERM:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextSecondary else SketchTextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = currentCard.front,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "(Tap to flip card)",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            } else {
                Column(
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ANSWER / EXPLANATION:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) NeonViolet else SketchTextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = currentCard.back,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    if (currentIndex > 0) {
                        currentIndex--
                        isFlipped = false
                    }
                },
                enabled = currentIndex > 0,
                modifier = Modifier.weight(1f)
            ) {
                Text("Previous")
            }

            NeonmanButton(
                text = if (currentIndex + 1 == items.size) "Restart" else "Next",
                onClick = {
                    if (currentIndex + 1 < items.size) {
                        currentIndex++
                        isFlipped = false
                    } else {
                        currentIndex = 0
                        isFlipped = false
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MatchingGameContent(
    pairs: List<MatchingPair>,
    isDark: Boolean
) {
    val context = LocalContext.current

    var selectedLeft by remember { mutableStateOf<String?>(null) }
    var selectedRight by remember { mutableStateOf<String?>(null) }

    val matchedPairs = remember { mutableStateListOf<String>() }

    val leftItems = remember(pairs) { pairs.map { it.left }.shuffled() }
    val rightItems = remember(pairs) { pairs.map { it.right }.shuffled() }

    fun checkMatch() {
        if (selectedLeft != null && selectedRight != null) {
            val isMatch = pairs.any { it.left == selectedLeft && it.right == selectedRight }
            if (isMatch) {
                matchedPairs.add(selectedLeft!!)
                Toast.makeText(context, "Pair matched correctly! 🎉", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Incorrect, please try again! ❌", Toast.LENGTH_SHORT).show()
            }
            selectedLeft = null
            selectedRight = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Select matching pairs from left and right columns:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) DarkTextPrimary else SketchTextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left Column
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                leftItems.forEach { item ->
                    val isMatched = matchedPairs.contains(item)
                    val isSelected = selectedLeft == item

                    Surface(
                        color = when {
                            isMatched -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                            isSelected -> if (isDark) NeonCyan.copy(alpha = 0.2f) else Color(0xFFE0F7FA)
                            else -> if (isDark) DarkCardPanel else Color.White
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (isSelected) (if (isDark) NeonCyan else SketchBorder) else (if (isDark) DarkBorder else SketchBorder),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(enabled = !isMatched) {
                                selectedLeft = item
                                checkMatch()
                            }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = item,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isMatched) Color(0xFF4CAF50) else (if (isDark) DarkTextPrimary else SketchTextPrimary)
                        )
                    }
                }
            }

            // Right Column
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                rightItems.forEach { item ->
                    val isMatched = pairs.any { it.right == item && matchedPairs.contains(it.left) }
                    val isSelected = selectedRight == item

                    Surface(
                        color = when {
                            isMatched -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                            isSelected -> if (isDark) NeonViolet.copy(alpha = 0.2f) else Color(0xFFF3E5F5)
                            else -> if (isDark) DarkCardPanel else Color.White
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (isSelected) (if (isDark) NeonViolet else SketchBorder) else (if (isDark) DarkBorder else SketchBorder),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(enabled = !isMatched) {
                                selectedRight = item
                                checkMatch()
                            }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = item,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isMatched) Color(0xFF4CAF50) else (if (isDark) DarkTextPrimary else SketchTextPrimary)
                        )
                    }
                }
            }
        }
    }
}
