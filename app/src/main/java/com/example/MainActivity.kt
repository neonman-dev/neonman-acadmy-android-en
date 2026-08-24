package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.datastore.UserPreferences
import com.example.ui.screens.*
import com.example.ui.theme.NeonmanTheme
import com.example.ui.viewmodels.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Splash : Screen()
    object Login : Screen()
    object Register : Screen()
    data class Main(val tab: Int = 0) : Screen()
    data class CourseDetail(val courseId: String) : Screen()
    data class LessonDetail(val courseId: String, val lessonIndex: Int) : Screen()
    object BooksList : Screen()
    data class BookDetail(val bookId: String) : Screen()
    object TestsList : Screen()
    data class TestRunner(val testId: String) : Screen()
    object GamesList : Screen()
    data class GamePlay(val gameId: String) : Screen()
    object PresentationsList : Screen()
    data class PresentationViewer(val presentationId: String) : Screen()
    object Schedule : Screen()
    object Saved : Screen()
    object BoardList : Screen()
    data class BoardCanvas(val boardId: String) : Screen()
    object AccountEdit : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userPrefs = UserPreferences(applicationContext)

        setContent {
            val tokenState by userPrefs.authToken.collectAsStateWithLifecycle(initialValue = null)
            val isDarkMode by userPrefs.isDarkMode.collectAsStateWithLifecycle(initialValue = true)

            val scope = rememberCoroutineScope()

            val authViewModel: AuthViewModel = viewModel()
            val courseViewModel: CourseViewModel = viewModel()
            val extrasViewModel: ExtrasViewModel = viewModel()
            val boardViewModel: BoardViewModel = viewModel()

            val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()
            val userProfile by authViewModel.userProfile.collectAsStateWithLifecycle()

            val courseListState by courseViewModel.courseListState.collectAsStateWithLifecycle()
            val courseDetailState by courseViewModel.courseDetailState.collectAsStateWithLifecycle()

            val courses = when (val state = courseListState) {
                is CourseListState.Success -> state.courses
                else -> emptyList()
            }
            val isCoursesLoading = courseListState is CourseListState.Loading

            val books by extrasViewModel.books.collectAsStateWithLifecycle()
            val selectedBook by extrasViewModel.selectedBook.collectAsStateWithLifecycle()

            val tests by extrasViewModel.tests.collectAsStateWithLifecycle()
            val selectedTest by extrasViewModel.selectedTest.collectAsStateWithLifecycle()

            val games by extrasViewModel.games.collectAsStateWithLifecycle()
            val selectedGame by extrasViewModel.selectedGame.collectAsStateWithLifecycle()

            val presentations by extrasViewModel.presentations.collectAsStateWithLifecycle()
            val selectedPresentation by extrasViewModel.selectedPresentation.collectAsStateWithLifecycle()

            val schedules by extrasViewModel.schedules.collectAsStateWithLifecycle()
            val savedItems by extrasViewModel.savedItems.collectAsStateWithLifecycle()

            val boards by boardViewModel.boards.collectAsStateWithLifecycle()
            val selectedBoard by boardViewModel.selectedBoard.collectAsStateWithLifecycle()
            val canvasPaths by boardViewModel.currentCanvasPaths.collectAsStateWithLifecycle()

            var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
            var currentTab by remember { mutableIntStateOf(0) }

            // On Token available, load initial data
            LaunchedEffect(tokenState) {
                tokenState?.let { tok ->
                    if (tok.isNotBlank()) {
                        authViewModel.fetchProfile(tok)
                        courseViewModel.loadCourses(tok)
                        extrasViewModel.loadBooks(tok)
                        extrasViewModel.loadTests(tok)
                        extrasViewModel.loadGames(tok)
                        extrasViewModel.loadPresentations(tok)
                        extrasViewModel.loadSchedule(tok)
                        extrasViewModel.loadSaved(tok)
                        boardViewModel.loadBoards(tok)
                    }
                }
            }

            // Observe Auth State transitions
            LaunchedEffect(authUiState) {
                when (val state = authUiState) {
                    is AuthUiState.Authenticated -> {
                        currentScreen = Screen.Main(tab = 0)
                    }
                    is AuthUiState.ProfileIncomplete -> {
                        currentScreen = Screen.Register
                    }
                    else -> {}
                }
            }

            NeonmanTheme(darkTheme = isDarkMode) {
                when (val screen = currentScreen) {
                    is Screen.Splash -> {
                        SplashScreen(
                            token = tokenState,
                            onNavigateNext = { hasToken ->
                                currentScreen = if (hasToken) Screen.Main(0) else Screen.Login
                            }
                        )
                    }

                    is Screen.Login -> {
                        LoginScreen(
                            onDemoLogin = { authViewModel.loginDemo() },
                            onTelegramLogin = { initData -> authViewModel.loginTelegram(initData) },
                            onGoogleLoginRequested = {
                                Toast.makeText(this@MainActivity, "Google sign-in will be available soon", Toast.LENGTH_SHORT).show()
                            },
                            isLoading = authUiState is AuthUiState.Loading,
                            errorMessage = (authUiState as? AuthUiState.Error)?.message
                        )
                    }

                    is Screen.Register -> {
                        RegisterScreen(
                            token = tokenState ?: "",
                            onComplete = { firstName, lastName, birthDate ->
                                tokenState?.let { tok ->
                                    authViewModel.completeProfile(tok, firstName, lastName, birthDate)
                                    currentScreen = Screen.Main(0)
                                }
                            },
                            isLoading = authUiState is AuthUiState.Loading
                        )
                    }

                    is Screen.Main -> {
                        MainScreen(
                            currentTab = currentTab,
                            onTabSelected = { currentTab = it },
                            token = tokenState,
                            userProfile = userProfile,
                            latestCourse = courses.firstOrNull(),
                            courses = courses,
                            isCoursesLoading = isCoursesLoading,
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { darkMode ->
                                authViewModel.setDarkMode(darkMode)
                            },
                            onOpenCourseDetail = { courseId ->
                                tokenState?.let { tok -> courseViewModel.loadCourseDetail(tok, courseId) }
                                currentScreen = Screen.CourseDetail(courseId)
                            },
                            onCreateCourseManual = { title, desc, lessons ->
                                tokenState?.let { tok ->
                                    courseViewModel.createCourseManual(
                                        token = tok,
                                        title = title,
                                        description = desc,
                                        lessons = lessons,
                                        onSuccess = {},
                                        onError = {}
                                    )
                                }
                            },
                            onNavigateFeature = { featureId ->
                                when (featureId) {
                                    "saved" -> {
                                        tokenState?.let { extrasViewModel.loadSaved(it) }
                                        currentScreen = Screen.Saved
                                    }
                                    "books" -> {
                                        tokenState?.let { extrasViewModel.loadBooks(it) }
                                        currentScreen = Screen.BooksList
                                    }
                                    "tests" -> {
                                        tokenState?.let { extrasViewModel.loadTests(it) }
                                        currentScreen = Screen.TestsList
                                    }
                                    "games" -> {
                                        tokenState?.let { extrasViewModel.loadGames(it) }
                                        currentScreen = Screen.GamesList
                                    }
                                    "presentations" -> {
                                        tokenState?.let { extrasViewModel.loadPresentations(it) }
                                        currentScreen = Screen.PresentationsList
                                    }
                                    "schedule" -> {
                                        tokenState?.let { extrasViewModel.loadSchedule(it) }
                                        currentScreen = Screen.Schedule
                                    }
                                    "board" -> {
                                        tokenState?.let { boardViewModel.loadBoards(it) }
                                        currentScreen = Screen.BoardList
                                    }
                                }
                            },
                            onNavigateAccount = {
                                currentScreen = Screen.AccountEdit
                            },
                            onLogout = {
                                authViewModel.logout()
                                currentScreen = Screen.Login
                            }
                        )
                    }

                    is Screen.CourseDetail -> {
                        CourseDetailScreen(
                            courseDetailState = courseDetailState,
                            onOpenLesson = { lessonIdx ->
                                currentScreen = Screen.LessonDetail(screen.courseId, lessonIdx)
                            },
                            onBackClick = { currentScreen = Screen.Main(tab = 1) }
                        )
                    }

                    is Screen.LessonDetail -> {
                        val currentCourse = (courseDetailState as? CourseDetailState.Success)?.course
                        val lessonItem = currentCourse?.lessons?.getOrNull(screen.lessonIndex)

                        if (lessonItem != null) {
                            LessonDetailScreen(
                                courseTitle = currentCourse.title,
                                lesson = lessonItem,
                                token = tokenState,
                                onSubmitAttempt = { selectedIndex, isCorrect ->
                                    tokenState?.let { tok ->
                                        courseViewModel.submitQuizAttempt(
                                            token = tok,
                                            contentType = "course",
                                            contentId = currentCourse.id,
                                            selectedIndex = selectedIndex,
                                            isCorrect = isCorrect,
                                            onResult = {}
                                        )
                                    }
                                },
                                onBackClick = { currentScreen = Screen.CourseDetail(screen.courseId) }
                            )
                        } else {
                            currentScreen = Screen.CourseDetail(screen.courseId)
                        }
                    }

                    is Screen.BooksList -> {
                        BooksListScreen(
                            books = books,
                            isLoading = isCoursesLoading,
                            onOpenBook = { bookId ->
                                tokenState?.let { tok -> extrasViewModel.loadBookDetail(tok, bookId) }
                                currentScreen = Screen.BookDetail(bookId)
                            },
                            onBackClick = { currentScreen = Screen.Main(tab = 2) }
                        )
                    }

                    is Screen.BookDetail -> {
                        BookDetailScreen(
                            bookDetail = selectedBook,
                            isLoading = isCoursesLoading,
                            onBackClick = { currentScreen = Screen.BooksList }
                        )
                    }

                    is Screen.TestsList -> {
                        TestsListScreen(
                            tests = tests,
                            isLoading = isCoursesLoading,
                            onOpenTest = { testId ->
                                tokenState?.let { tok -> extrasViewModel.loadTestDetail(tok, testId) }
                                currentScreen = Screen.TestRunner(testId)
                            },
                            onBackClick = { currentScreen = Screen.Main(tab = 2) }
                        )
                    }

                    is Screen.TestRunner -> {
                        TestRunnerScreen(
                            testDetail = selectedTest,
                            isLoading = isCoursesLoading,
                            onBackClick = { currentScreen = Screen.TestsList }
                        )
                    }

                    is Screen.GamesList -> {
                        GamesListScreen(
                            games = games,
                            isLoading = isCoursesLoading,
                            onOpenGame = { gameId ->
                                tokenState?.let { tok -> extrasViewModel.loadGameDetail(tok, gameId) }
                                currentScreen = Screen.GamePlay(gameId)
                            },
                            onBackClick = { currentScreen = Screen.Main(tab = 2) }
                        )
                    }

                    is Screen.GamePlay -> {
                        GamePlayScreen(
                            gameDetail = selectedGame,
                            isLoading = isCoursesLoading,
                            onBackClick = { currentScreen = Screen.GamesList }
                        )
                    }

                    is Screen.PresentationsList -> {
                        PresentationsListScreen(
                            presentations = presentations,
                            isLoading = isCoursesLoading,
                            onOpenPresentation = { presentationId ->
                                tokenState?.let { tok -> extrasViewModel.loadPresentationDetail(tok, presentationId) }
                                currentScreen = Screen.PresentationViewer(presentationId)
                            },
                            onBackClick = { currentScreen = Screen.Main(tab = 2) }
                        )
                    }

                    is Screen.PresentationViewer -> {
                        PresentationViewerScreen(
                            presentationDetail = selectedPresentation,
                            isLoading = isCoursesLoading,
                            onBackClick = { currentScreen = Screen.PresentationsList }
                        )
                    }

                    is Screen.Schedule -> {
                        ScheduleScreen(
                            schedules = schedules,
                            isLoading = isCoursesLoading,
                            onAddSchedule = { title, dayLabel, startTime, endTime, notes ->
                                tokenState?.let { tok ->
                                    extrasViewModel.addSchedule(
                                        token = tok,
                                        title = title,
                                        dayLabel = dayLabel,
                                        startTime = startTime,
                                        endTime = endTime,
                                        notes = notes,
                                        onDone = {}
                                    )
                                }
                            },
                            onDeleteSchedule = { id ->
                                tokenState?.let { tok -> extrasViewModel.deleteSchedule(tok, id) }
                            },
                            onBackClick = { currentScreen = Screen.Main(tab = 2) }
                        )
                    }

                    is Screen.Saved -> {
                        SavedScreen(
                            savedItems = savedItems,
                            isLoading = isCoursesLoading,
                            onDeleteSaved = { id ->
                                tokenState?.let { tok -> extrasViewModel.deleteSaved(tok, id) }
                            },
                            onBackClick = { currentScreen = Screen.Main(tab = 2) }
                        )
                    }

                    is Screen.BoardList -> {
                        BoardListScreen(
                            boards = boards,
                            isLoading = isCoursesLoading,
                            onOpenBoard = { boardId ->
                                tokenState?.let { tok -> boardViewModel.loadBoardDetail(tok, boardId) }
                                currentScreen = Screen.BoardCanvas(boardId)
                            },
                            onCreateBoard = { boardTitle ->
                                tokenState?.let { tok ->
                                    boardViewModel.createBoard(tok, boardTitle) { newId ->
                                        boardViewModel.loadBoardDetail(tok, newId)
                                        currentScreen = Screen.BoardCanvas(newId)
                                    }
                                }
                            },
                            onBackClick = { currentScreen = Screen.Main(tab = 2) }
                        )
                    }

                    is Screen.BoardCanvas -> {
                        AiBoardCanvasScreen(
                            boardTitle = selectedBoard?.title ?: "AI Whiteboard",
                            initialPaths = canvasPaths,
                            onSaveCanvas = { paths ->
                                boardViewModel.updatePaths(paths)
                                boardViewModel.saveBoardCanvas(screen.boardId) {}
                            },
                            onBackClick = { currentScreen = Screen.BoardList }
                        )
                    }

                    is Screen.AccountEdit -> {
                        AccountEditScreen(
                            userProfile = userProfile,
                            onSaveProfile = { firstName, lastName, birthDate ->
                                tokenState?.let { tok ->
                                    authViewModel.updateAccountProfile(
                                        token = tok,
                                        firstName = firstName,
                                        lastName = lastName,
                                        birthDate = birthDate,
                                        onSuccess = {}
                                    )
                                }
                            },
                            onBackClick = { currentScreen = Screen.Main(tab = 3) }
                        )
                    }
                }
            }
        }
    }
}
