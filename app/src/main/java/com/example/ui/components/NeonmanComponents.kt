package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun NeonmanCard(
    modifier: Modifier = Modifier,
    isDark: Boolean = LocalExtraThemeColors.current.isDark,
    onClick: (() -> Unit)? = null,
    cornerSize: Dp = 12.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerSize)
    val cardModifier = if (onClick != null) {
        modifier
            .clip(shape)
            .clickable { onClick() }
    } else {
        modifier.clip(shape)
    }

    if (isDark) {
        // Neon Dark Mode: Card/Panel background with glowing cyan/violet border
        Box(
            modifier = cardModifier
                .background(DarkCardPanel, shape)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.6f), NeonViolet.copy(alpha = 0.6f))),
                    shape = shape
                )
                .padding(16.dp)
        ) {
            Column(content = content)
        }
    } else {
        // Sketch Light Mode: White background with 2dp thick black border
        Box(
            modifier = cardModifier
                .background(Color.White, shape)
                .border(width = 2.dp, color = SketchBorder, shape = shape)
                .padding(16.dp)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun NeonmanButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = LocalExtraThemeColors.current.isDark,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    testTagStr: String = "action_button"
) {
    val shape = RoundedCornerShape(8.dp)

    if (isDark) {
        // Neon Gradient Cyan->Violet Button
        val brush = if (enabled) {
            Brush.horizontalGradient(listOf(NeonCyan, NeonViolet))
        } else {
            Brush.horizontalGradient(listOf(Color.Gray, Color.DarkGray))
        }

        Box(
            modifier = modifier
                .testTag(testTagStr)
                .height(48.dp)
                .clip(shape)
                .background(brush)
                .clickable(enabled = enabled) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = DarkBackground,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = DarkBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    } else {
        // Sketch Mode: White background + 2dp thick black border + Black text
        Box(
            modifier = modifier
                .testTag(testTagStr)
                .height(48.dp)
                .clip(shape)
                .background(if (enabled) Color.White else Color(0xFFEEEEEE))
                .border(2.dp, if (enabled) SketchBorder else Color.Gray, shape)
                .clickable(enabled = enabled) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (enabled) SketchBorder else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = if (enabled) SketchBorder else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun NeonmanOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = LocalExtraThemeColors.current.isDark,
    icon: ImageVector? = null
) {
    val shape = RoundedCornerShape(8.dp)

    val borderColor = if (isDark) NeonCyan else SketchBorder
    val textColor = if (isDark) NeonCyan else SketchBorder

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .border(2.dp, borderColor, shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun NeonmanTopBar(
    title: String,
    onMenuClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    onMoreClick: (() -> Unit)? = null,
    isDark: Boolean = LocalExtraThemeColors.current.isDark
) {
    Surface(
        color = if (isDark) DarkElevatedSurface else Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isDark) 0.dp else 2.dp,
                color = if (isDark) Color.Transparent else SketchBorder
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isDark) DarkTextPrimary else SketchTextPrimary
                    )
                }
            } else if (onMenuClick != null) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = if (isDark) DarkTextPrimary else SketchTextPrimary
                    )
                }
            } else {
                // Placeholder logo N
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDark) DarkCardPanel else Color.White)
                        .border(
                            2.dp,
                            if (isDark) NeonCyan else SketchBorder,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "N",
                        color = if (isDark) NeonCyan else SketchTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            if (onSearchClick != null) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = if (isDark) Icons.Default.Search else Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = if (isDark) DarkTextPrimary else SketchTextPrimary
                    )
                }
            }

            if (onMoreClick != null) {
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = if (isDark) DarkTextPrimary else SketchTextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun NeonmanBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    isDark: Boolean = LocalExtraThemeColors.current.isDark
) {
    val backgroundColor = if (isDark) DarkElevatedSurface else Color.White
    val borderModifier = if (isDark) {
        Modifier.border(1.dp, DarkBorder)
    } else {
        Modifier.border(2.dp, SketchBorder)
    }

    Surface(
        color = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .then(borderModifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                label = "Home",
                icon = if (isDark) Icons.Default.Home else Icons.Outlined.Home,
                isSelected = selectedTab == 0,
                isDark = isDark,
                onClick = { onTabSelected(0) }
            )
            BottomNavItem(
                label = "Courses",
                icon = if (isDark) Icons.Default.School else Icons.Outlined.School,
                isSelected = selectedTab == 1,
                isDark = isDark,
                onClick = { onTabSelected(1) }
            )
            BottomNavItem(
                label = "Features",
                icon = if (isDark) Icons.Default.Apps else Icons.Outlined.Apps,
                isSelected = selectedTab == 2,
                isDark = isDark,
                onClick = { onTabSelected(2) }
            )
            BottomNavItem(
                label = "Profile",
                icon = if (isDark) Icons.Default.Person else Icons.Outlined.Person,
                isSelected = selectedTab == 3,
                isDark = isDark,
                onClick = { onTabSelected(3) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val activeColor = if (isDark) NeonCyan else SketchTextPrimary
    val inactiveColor = if (isDark) DarkTextSecondary else Color.Gray

    val tintColor by animateColorAsState(if (isSelected) activeColor else inactiveColor, label = "color")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tintColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = tintColor
        )
    }
}
