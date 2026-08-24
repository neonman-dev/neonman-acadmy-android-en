package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CourseListState {
    object Loading : CourseListState
    data class Success(val courses: List<CourseItem>) : CourseListState
    data class Error(val message: String) : CourseListState
}

sealed interface CourseDetailState {
    object Loading : CourseDetailState
    data class Success(val course: CourseDetail) : CourseDetailState
    data class Error(val message: String) : CourseDetailState
}

class CourseViewModel : ViewModel() {

    private val _courseListState = MutableStateFlow<CourseListState>(CourseListState.Loading)
    val courseListState: StateFlow<CourseListState> = _courseListState.asStateFlow()

    private val _courseDetailState = MutableStateFlow<CourseDetailState>(CourseDetailState.Loading)
    val courseDetailState: StateFlow<CourseDetailState> = _courseDetailState.asStateFlow()

    private val _isCreatingCourse = MutableStateFlow(false)
    val isCreatingCourse: StateFlow<Boolean> = _isCreatingCourse.asStateFlow()

    fun loadCourses(token: String) {
        viewModelScope.launch {
            _courseListState.value = CourseListState.Loading
            try {
                val response = ApiClient.service.getCourses(token)
                _courseListState.value = CourseListState.Success(response.courses)
            } catch (e: Exception) {
                _courseListState.value = CourseListState.Error(e.localizedMessage ?: "Kurslarni yuklab bo'lmadi")
            }
        }
    }

    fun loadCourseDetail(token: String, courseId: String) {
        viewModelScope.launch {
            _courseDetailState.value = CourseDetailState.Loading
            try {
                val detail = ApiClient.service.getCourseDetail(courseId, token)
                _courseDetailState.value = CourseDetailState.Success(detail)
            } catch (e: Exception) {
                _courseDetailState.value = CourseDetailState.Error(e.localizedMessage ?: "Kurs ma'lumotini yuklab bo'lmadi")
            }
        }
    }

    fun createCourseManual(
        token: String,
        title: String,
        description: String,
        lessons: List<CreateLessonRequest>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isCreatingCourse.value = true
            try {
                val req = CreateCourseRequest(
                    token = token,
                    title = title,
                    description = description,
                    lessons = lessons
                )
                ApiClient.service.createCourseManual(req)
                loadCourses(token)
                _isCreatingCourse.value = false
                onSuccess()
            } catch (e: Exception) {
                _isCreatingCourse.value = false
                onError(e.localizedMessage ?: "Kurs yaratishda xatolik yuz berdi")
            }
        }
    }

    fun submitQuizAttempt(
        token: String,
        contentType: String,
        contentId: String,
        selectedIndex: Int,
        isCorrect: Boolean,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                ApiClient.service.recordAttempt(
                    AttemptRequest(
                        token = token,
                        contentType = contentType,
                        contentId = contentId,
                        selectedIndex = selectedIndex,
                        isCorrect = isCorrect
                    )
                )
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}
