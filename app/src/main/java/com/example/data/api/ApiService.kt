package com.example.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    // Auth
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest = LoginRequest()): LoginResponse

    @POST("/api/auth/telegram")
    suspend fun authTelegram(@Body request: TelegramAuthRequest): TelegramAuthResponse

    // Profile
    @GET("/api/profile")
    suspend fun getProfile(@Query("token") token: String): ProfileResponse

    @PATCH("/api/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): ProfileResponse

    // Courses
    @GET("/api/courses")
    suspend fun getCourses(@Query("token") token: String): CoursesResponse

    @GET("/api/courses/{id}")
    suspend fun getCourseDetail(@Path("id") id: String, @Query("token") token: String): CourseDetail

    @POST("/api/courses/manual")
    suspend fun createCourseManual(@Body request: CreateCourseRequest): CourseDetail

    @PATCH("/api/courses/{id}")
    suspend fun updateCourse(@Path("id") id: String, @Body body: Map<String, String>): GenericResponse

    @DELETE("/api/courses/{id}")
    suspend fun deleteCourse(@Path("id") id: String): GenericResponse

    // Tests
    @GET("/api/tests")
    suspend fun getTests(@Query("token") token: String): TestsResponse

    @GET("/api/tests/{id}")
    suspend fun getTestDetail(@Path("id") id: String, @Query("token") token: String): TestDetailWrapper

    @POST("/api/tests")
    suspend fun createTest(@Body request: CreateTestRequest): GenericResponse

    // Games
    @GET("/api/games")
    suspend fun getGames(@Query("token") token: String): GamesResponse

    @GET("/api/games/{id}")
    suspend fun getGameDetail(@Path("id") id: String, @Query("token") token: String): GameDetailWrapper

    // Presentations
    @GET("/api/presentations")
    suspend fun getPresentations(@Query("token") token: String): PresentationsResponse

    @GET("/api/presentations/{id}")
    suspend fun getPresentationDetail(@Path("id") id: String, @Query("token") token: String): PresentationDetailWrapper

    // Books
    @GET("/api/books")
    suspend fun getBooks(@Query("token") token: String): BooksResponse

    @GET("/api/books/{id}")
    suspend fun getBookDetail(@Path("id") id: String, @Query("token") token: String): BookDetailWrapper

    // Schedule
    @GET("/api/schedule")
    suspend fun getSchedule(@Query("token") token: String): ScheduleResponse

    @POST("/api/schedule")
    suspend fun createSchedule(@Body request: CreateScheduleRequest): GenericResponse

    @DELETE("/api/schedule/{id}")
    suspend fun deleteSchedule(@Path("id") id: String): GenericResponse

    // Saved
    @GET("/api/saved")
    suspend fun getSaved(@Query("token") token: String): SavedResponse

    @POST("/api/saved")
    suspend fun saveItem(@Body request: SaveItemRequest): GenericResponse

    @DELETE("/api/saved/{id}")
    suspend fun deleteSavedItem(@Path("id") id: String): GenericResponse

    // AI Boards
    @GET("/api/boards")
    suspend fun getBoards(@Query("token") token: String): BoardsResponse

    @GET("/api/boards/{id}")
    suspend fun getBoardDetail(@Path("id") id: String, @Query("token") token: String): BoardDetailWrapper

    @POST("/api/boards")
    suspend fun createBoard(@Body request: CreateBoardRequest): BoardDetailWrapper

    @PUT("/api/boards/{id}")
    suspend fun updateBoard(@Path("id") id: String, @Body request: UpdateBoardRequest): GenericResponse

    // Quiz Attempts
    @POST("/api/attempts")
    suspend fun recordAttempt(@Body request: AttemptRequest): GenericResponse
}

object ApiClient {
    private const val BASE_URL = "https://mvp-neonman-academy.vercel.app"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val service: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }
}
