package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.BoardItem
import com.example.data.api.DrawPathData
import com.example.data.api.DrawPointData
import com.example.ui.components.NeonmanButton
import com.example.ui.components.NeonmanCard
import com.example.ui.components.NeonmanTopBar
import com.example.ui.theme.*

@Composable
fun BoardListScreen(
    boards: List<BoardItem>,
    isLoading: Boolean,
    onOpenBoard: (String) -> Unit,
    onCreateBoard: (title: String) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = "AI Whiteboard",
                onBackClick = onBackClick,
                isDark = isDark
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = if (isDark) NeonCyan else Color.White,
                contentColor = if (isDark) DarkBackground else SketchBorder,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create Board")
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
            if (isLoading && boards.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) NeonCyan else SketchBorder)
                }
            } else if (boards.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No whiteboards found. Tap '+' to create a new board.", color = if (isDark) DarkTextSecondary else SketchTextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(boards) { board ->
                        NeonmanCard(
                            isDark = isDark,
                            onClick = { onOpenBoard(board.id) },
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
                                        imageVector = Icons.Default.Draw,
                                        contentDescription = null,
                                        tint = if (isDark) NeonCyan else SketchTextPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = board.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) DarkTextPrimary else SketchTextPrimary
                                    )
                                    if (!board.updatedAt.isNullOrEmpty()) {
                                        Text(
                                            text = board.updatedAt,
                                            fontSize = 12.sp,
                                            color = if (isDark) DarkTextSecondary else SketchTextSecondary
                                        )
                                    }
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

    if (showCreateDialog) {
        var boardTitle by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create New AI Whiteboard", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = boardTitle,
                    onValueChange = { boardTitle = it },
                    label = { Text("Board Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (boardTitle.isNotBlank()) {
                            onCreateBoard(boardTitle)
                            showCreateDialog = false
                        }
                    },
                    enabled = boardTitle.isNotBlank()
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) { Text("Cancel") }
            },
            containerColor = if (isDark) DarkElevatedSurface else Color.White
        )
    }
}

@Composable
fun AiBoardCanvasScreen(
    boardTitle: String,
    initialPaths: List<DrawPathData>,
    onSaveCanvas: (List<DrawPathData>) -> Unit,
    onBackClick: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    val context = LocalContext.current

    val paths = remember { mutableStateListOf<DrawPathData>().apply { addAll(initialPaths) } }

    var selectedColorHex by remember { mutableStateOf("#00E5FF") }
    var strokeWidthPx by remember { mutableFloatStateOf(6f) }

    var currentPoints = remember { mutableStateListOf<DrawPointData>() }

    val colorOptions = listOf(
        "#00E5FF", // Cyan
        "#B14EFF", // Violet
        "#FFFFFF", // White
        "#1A1A1A", // Black
        "#FF5C7A", // Danger Red
        "#81C784", // Green
        "#FFB74D"  // Yellow
    )

    Scaffold(
        topBar = {
            NeonmanTopBar(
                title = boardTitle.ifBlank { "AI Whiteboard" },
                onBackClick = onBackClick,
                onMoreClick = null,
                isDark = isDark
            )
        },
        containerColor = if (isDark) DarkBackground else SketchBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Controls Palette Top Bar
            Surface(
                color = if (isDark) DarkElevatedSurface else Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (isDark) DarkBorder else SketchBorder)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Color Palette selector
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        colorOptions.forEach { hex ->
                            val color = Color(android.graphics.Color.parseColor(hex))
                            val isSelected = selectedColorHex == hex

                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) (if (isDark) NeonCyan else Color.Black) else Color.Gray,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColorHex = hex }
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if (paths.isNotEmpty()) {
                                paths.removeAt(paths.size - 1)
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Undo, contentDescription = "Undo", tint = if (isDark) DarkTextPrimary else SketchTextPrimary)
                        }

                        IconButton(onClick = { paths.clear() }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = NeonDanger)
                        }

                        IconButton(onClick = {
                            onSaveCanvas(paths.toList())
                            Toast.makeText(context, "Board saved! 💾", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = "Save", tint = if (isDark) NeonCyan else SketchTextPrimary)
                        }
                    }
                }
            }

            // Finger Canvas Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .sketchGridBackground(isDark)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPoints.clear()
                                    currentPoints.add(DrawPointData(offset.x, offset.y))
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentPoints.add(DrawPointData(change.position.x, change.position.y))
                                },
                                onDragEnd = {
                                    if (currentPoints.isNotEmpty()) {
                                        paths.add(
                                            DrawPathData(
                                                colorHex = selectedColorHex,
                                                strokeWidth = strokeWidthPx,
                                                points = currentPoints.toList()
                                            )
                                        )
                                        currentPoints.clear()
                                    }
                                }
                            )
                        }
                ) {
                    // Draw saved paths
                    paths.forEach { drawPath ->
                        if (drawPath.points.size > 1) {
                            val color = try {
                                Color(android.graphics.Color.parseColor(drawPath.colorHex))
                            } catch (e: Exception) {
                                NeonCyan
                            }
                            val p = Path().apply {
                                moveTo(drawPath.points[0].x, drawPath.points[0].y)
                                for (i in 1 until drawPath.points.size) {
                                    lineTo(drawPath.points[i].x, drawPath.points[i].y)
                                }
                            }
                            drawPath(
                                path = p,
                                color = color,
                                style = Stroke(
                                    width = drawPath.strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }

                    // Draw current dragging line
                    if (currentPoints.size > 1) {
                        val color = try {
                            Color(android.graphics.Color.parseColor(selectedColorHex))
                        } catch (e: Exception) {
                            NeonCyan
                        }
                        val p = Path().apply {
                            moveTo(currentPoints[0].x, currentPoints[0].y)
                            for (i in 1 until currentPoints.size) {
                                lineTo(currentPoints[i].x, currentPoints[i].y)
                            }
                        }
                        drawPath(
                            path = p,
                            color = color,
                            style = Stroke(
                                width = strokeWidthPx,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }
        }
    }
}
