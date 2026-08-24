package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.api.CourseItem
import com.example.data.api.UserProfile
import com.example.ui.components.NeonmanBottomNavigation
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    token: String?,
    userProfile: UserProfile?,
    latestCourse: CourseItem?,
    courses: List<CourseItem>,
    isCoursesLoading: Boolean,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onOpenCourseDetail: (String) -> Unit,
    onCreateCourseManual: (title: String, description: String, lessons: List<com.example.data.api.CreateLessonRequest>) -> Unit,
    onNavigateFeature: (String) -> Unit,
    onNavigateAccount: () -> Unit,
    onLogout: () -> Unit
) {
    val isDark = LocalExtraThemeColors.current.isDark
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = if (isDark) DarkElevatedSurface else Color.White,
                drawerContentColor = if (isDark) DarkTextPrimary else SketchTextPrimary,
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header inside Drawer
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isDark) DarkCardPanel else Color.White)
                                .border(2.dp, if (isDark) NeonCyan else SketchBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_neonman_logo),
                                contentDescription = "Neonman Logo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Neonman Academy",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) DarkTextPrimary else SketchTextPrimary
                            )
                            Text(
                                text = userProfile?.firstName ?: "Student",
                                fontSize = 12.sp,
                                color = if (isDark) DarkTextSecondary else SketchTextSecondary
                            )
                        }
                    }

                    HorizontalDivider(color = if (isDark) DarkBorder else SketchBorder)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Drawer Items
                    DrawerNavItem("Home", Icons.Default.Home, isDark) {
                        scope.launch { drawerState.close() }
                        onTabSelected(0)
                    }
                    DrawerNavItem("Courses", Icons.Default.School, isDark) {
                        scope.launch { drawerState.close() }
                        onTabSelected(1)
                    }
                    DrawerNavItem("Features", Icons.Default.Apps, isDark) {
                        scope.launch { drawerState.close() }
                        onTabSelected(2)
                    }
                    DrawerNavItem("Saved", Icons.Default.Bookmark, isDark) {
                        scope.launch { drawerState.close() }
                        onNavigateFeature("saved")
                    }
                    DrawerNavItem("Books", Icons.Default.MenuBook, isDark) {
                        scope.launch { drawerState.close() }
                        onNavigateFeature("books")
                    }
                    DrawerNavItem("Tests", Icons.Default.Quiz, isDark) {
                        scope.launch { drawerState.close() }
                        onNavigateFeature("tests")
                    }
                    DrawerNavItem("Games", Icons.Default.SportsEsports, isDark) {
                        scope.launch { drawerState.close() }
                        onNavigateFeature("games")
                    }
                    DrawerNavItem("Presentations", Icons.Default.Slideshow, isDark) {
                        scope.launch { drawerState.close() }
                        onNavigateFeature("presentations")
                    }
                    DrawerNavItem("Schedule", Icons.Default.CalendarMonth, isDark) {
                        scope.launch { drawerState.close() }
                        onNavigateFeature("schedule")
                    }
                    DrawerNavItem("AI Whiteboard", Icons.Default.Draw, isDark) {
                        scope.launch { drawerState.close() }
                        onNavigateFeature("board")
                    }
                    DrawerNavItem("Settings", Icons.Default.Settings, isDark) {
                        scope.launch { drawerState.close() }
                        onTabSelected(3)
                    }
                }
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                NeonmanBottomNavigation(
                    selectedTab = currentTab,
                    onTabSelected = onTabSelected,
                    isDark = isDark
                )
            },
            containerColor = if (isDark) DarkBackground else SketchBackground
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    0 -> HomeScreen(
                        latestCourse = latestCourse,
                        userName = userProfile?.firstName,
                        onOpenCourseDetail = onOpenCourseDetail,
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onSearchClick = { onTabSelected(1) },
                        onMoreClick = { onTabSelected(3) }
                    )
                    1 -> CoursesScreen(
                        token = token,
                        courses = courses,
                        isLoading = isCoursesLoading,
                        onOpenCourseDetail = onOpenCourseDetail,
                        onCreateCourseManual = onCreateCourseManual,
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )
                    2 -> ExtrasScreen(
                        onNavigateFeature = onNavigateFeature,
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )
                    3 -> SettingsScreen(
                        userProfile = userProfile,
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = onToggleDarkMode,
                        onNavigateAccount = onNavigateAccount,
                        onNavigateExtras = { onTabSelected(2) },
                        onLogout = onLogout,
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerNavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDark: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isDark) NeonCyan else SketchTextPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDark) DarkTextPrimary else SketchTextPrimary
            )
        }
    }
}
