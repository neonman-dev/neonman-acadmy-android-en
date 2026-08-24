package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val username: String = "demo",
    val password: String = "demo2026"
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val token: String?,
    val username: String?
)

@JsonClass(generateAdapter = true)
data class TelegramAuthRequest(
    val initData: String
)

@JsonClass(generateAdapter = true)
data class TelegramAuthResponse(
    val token: String?,
    val user: UserProfile?
)

@JsonClass(generateAdapter = true)
data class UserProfile(
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: String? = null,
    val provider: String? = null,
    val role: String? = null
)

@JsonClass(generateAdapter = true)
data class ProfileResponse(
    val user: UserProfile?
)

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    val token: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String
)

@JsonClass(generateAdapter = true)
data class CourseItem(
    val id: String,
    val title: String,
    val description: String? = null,
    val lessonCount: Int? = 0,
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class CoursesResponse(
    val courses: List<CourseItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class QuizQuestion(
    val id: String? = null,
    val question: String,
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0
)

@JsonClass(generateAdapter = true)
data class LessonItem(
    val id: String? = null,
    val title: String,
    val content: String,
    val quiz: List<QuizQuestion>? = null
)

@JsonClass(generateAdapter = true)
data class CourseDetail(
    val id: String,
    val title: String,
    val description: String? = null,
    val lessons: List<LessonItem> = emptyList(),
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateLessonRequest(
    val title: String,
    val content: String,
    val quiz: List<QuizQuestion>? = null
)

@JsonClass(generateAdapter = true)
data class CreateCourseRequest(
    val token: String,
    val title: String,
    val description: String,
    val lessons: List<CreateLessonRequest>
)

@JsonClass(generateAdapter = true)
data class TestItem(
    val id: String,
    val title: String,
    val description: String? = null,
    val questionCount: Int? = 0,
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class TestsResponse(
    val tests: List<TestItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TestDetail(
    val id: String,
    val title: String,
    val questions: List<QuizQuestion> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TestDetailWrapper(
    val test: TestDetail
)

@JsonClass(generateAdapter = true)
data class CreateTestRequest(
    val token: String,
    val title: String,
    val description: String,
    val questions: List<QuizQuestion>
)

@JsonClass(generateAdapter = true)
data class GameItem(
    val id: String,
    val title: String,
    val type: String,
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class GamesResponse(
    val games: List<GameItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class FlashcardItem(
    val front: String,
    val back: String
)

@JsonClass(generateAdapter = true)
data class MatchingPair(
    val left: String,
    val right: String
)

@JsonClass(generateAdapter = true)
data class GameData(
    val items: List<FlashcardItem>? = null,
    val pairs: List<MatchingPair>? = null
)

@JsonClass(generateAdapter = true)
data class GameDetail(
    val id: String,
    val title: String,
    val type: String,
    val data: GameData? = null
)

@JsonClass(generateAdapter = true)
data class GameDetailWrapper(
    val game: GameDetail
)

@JsonClass(generateAdapter = true)
data class PresentationItem(
    val id: String,
    val title: String,
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class PresentationsResponse(
    val presentations: List<PresentationItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class SlideItem(
    val title: String,
    val bullets: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PresentationDetail(
    val title: String,
    val slides: List<SlideItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PresentationDetailWrapper(
    val presentation: PresentationDetail
)

@JsonClass(generateAdapter = true)
data class BookItem(
    val id: String,
    val title: String,
    val description: String? = null,
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class BooksResponse(
    val books: List<BookItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ChapterItem(
    val title: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class BookDetail(
    val title: String,
    val chapters: List<ChapterItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class BookDetailWrapper(
    val book: BookDetail
)

@JsonClass(generateAdapter = true)
data class ScheduleItem(
    val id: String,
    val title: String,
    val dayLabel: String,
    val startTime: String,
    val endTime: String,
    val notes: String? = null
)

@JsonClass(generateAdapter = true)
data class ScheduleResponse(
    val items: List<ScheduleItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class CreateScheduleRequest(
    val token: String,
    val title: String,
    val dayLabel: String,
    val startTime: String,
    val endTime: String,
    val notes: String? = null
)

@JsonClass(generateAdapter = true)
data class SavedItem(
    val id: String,
    val itemType: String,
    val itemId: String
)

@JsonClass(generateAdapter = true)
data class SavedResponse(
    val items: List<SavedItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class SaveItemRequest(
    val token: String,
    val itemType: String,
    val itemId: String
)

@JsonClass(generateAdapter = true)
data class BoardItem(
    val id: String,
    val title: String,
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class BoardsResponse(
    val boards: List<BoardItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class DrawPointData(
    val x: Float,
    val y: Float
)

@JsonClass(generateAdapter = true)
data class DrawPathData(
    val colorHex: String,
    val strokeWidth: Float,
    val points: List<DrawPointData> = emptyList()
)

@JsonClass(generateAdapter = true)
data class BoardDetail(
    val id: String,
    val title: String,
    val canvas: List<DrawPathData> = emptyList()
)

@JsonClass(generateAdapter = true)
data class BoardDetailWrapper(
    val board: BoardDetail
)

@JsonClass(generateAdapter = true)
data class CreateBoardRequest(
    val token: String,
    val title: String
)

@JsonClass(generateAdapter = true)
data class UpdateBoardRequest(
    val canvas: List<DrawPathData>
)

@JsonClass(generateAdapter = true)
data class AttemptRequest(
    val token: String,
    val contentType: String,
    val contentId: String,
    val selectedIndex: Int,
    val isCorrect: Boolean
)

@JsonClass(generateAdapter = true)
data class GenericResponse(
    val success: Boolean? = true,
    val message: String? = null
)
