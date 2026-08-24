package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExtrasViewModel : ViewModel() {

    // Tests State
    private val _tests = MutableStateFlow<List<TestItem>>(emptyList())
    val tests: StateFlow<List<TestItem>> = _tests.asStateFlow()

    private val _selectedTest = MutableStateFlow<TestDetail?>(null)
    val selectedTest: StateFlow<TestDetail?> = _selectedTest.asStateFlow()

    // Games State
    private val _games = MutableStateFlow<List<GameItem>>(emptyList())
    val games: StateFlow<List<GameItem>> = _games.asStateFlow()

    private val _selectedGame = MutableStateFlow<GameDetail?>(null)
    val selectedGame: StateFlow<GameDetail?> = _selectedGame.asStateFlow()

    // Presentations State
    private val _presentations = MutableStateFlow<List<PresentationItem>>(emptyList())
    val presentations: StateFlow<List<PresentationItem>> = _presentations.asStateFlow()

    private val _selectedPresentation = MutableStateFlow<PresentationDetail?>(null)
    val selectedPresentation: StateFlow<PresentationDetail?> = _selectedPresentation.asStateFlow()

    // Books State
    private val _books = MutableStateFlow<List<BookItem>>(emptyList())
    val books: StateFlow<List<BookItem>> = _books.asStateFlow()

    private val _selectedBook = MutableStateFlow<BookDetail?>(null)
    val selectedBook: StateFlow<BookDetail?> = _selectedBook.asStateFlow()

    // Schedule State
    private val _schedules = MutableStateFlow<List<ScheduleItem>>(emptyList())
    val schedules: StateFlow<List<ScheduleItem>> = _schedules.asStateFlow()

    // Saved Items State
    private val _savedItems = MutableStateFlow<List<SavedItem>>(emptyList())
    val savedItems: StateFlow<List<SavedItem>> = _savedItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadTests(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = ApiClient.service.getTests(token)
                _tests.value = res.tests
            } catch (e: Exception) {
                _tests.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTestDetail(token: String, id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val wrapper = ApiClient.service.getTestDetail(id, token)
                _selectedTest.value = wrapper.test
            } catch (e: Exception) {
                _selectedTest.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadGames(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = ApiClient.service.getGames(token)
                _games.value = res.games
            } catch (e: Exception) {
                _games.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadGameDetail(token: String, id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val wrapper = ApiClient.service.getGameDetail(id, token)
                _selectedGame.value = wrapper.game
            } catch (e: Exception) {
                _selectedGame.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadPresentations(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = ApiClient.service.getPresentations(token)
                _presentations.value = res.presentations
            } catch (e: Exception) {
                _presentations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadPresentationDetail(token: String, id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val wrapper = ApiClient.service.getPresentationDetail(id, token)
                _selectedPresentation.value = wrapper.presentation
            } catch (e: Exception) {
                _selectedPresentation.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBooks(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = ApiClient.service.getBooks(token)
                _books.value = res.books
            } catch (e: Exception) {
                _books.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBookDetail(token: String, id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val wrapper = ApiClient.service.getBookDetail(id, token)
                _selectedBook.value = wrapper.book
            } catch (e: Exception) {
                _selectedBook.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadSchedule(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = ApiClient.service.getSchedule(token)
                _schedules.value = res.items
            } catch (e: Exception) {
                _schedules.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addSchedule(
        token: String,
        title: String,
        dayLabel: String,
        startTime: String,
        endTime: String,
        notes: String?,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                ApiClient.service.createSchedule(
                    CreateScheduleRequest(
                        token = token,
                        title = title,
                        dayLabel = dayLabel,
                        startTime = startTime,
                        endTime = endTime,
                        notes = notes
                    )
                )
                loadSchedule(token)
                onDone()
            } catch (e: Exception) {
                // handle
            }
        }
    }

    fun deleteSchedule(token: String, id: String) {
        viewModelScope.launch {
            try {
                ApiClient.service.deleteSchedule(id)
                loadSchedule(token)
            } catch (e: Exception) {
                // handle
            }
        }
    }

    fun loadSaved(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = ApiClient.service.getSaved(token)
                _savedItems.value = res.items
            } catch (e: Exception) {
                _savedItems.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveItem(token: String, itemType: String, itemId: String, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                ApiClient.service.saveItem(SaveItemRequest(token, itemType, itemId))
                loadSaved(token)
                onDone()
            } catch (e: Exception) {
                // handle
            }
        }
    }

    fun deleteSaved(token: String, id: String) {
        viewModelScope.launch {
            try {
                ApiClient.service.deleteSavedItem(id)
                loadSaved(token)
            } catch (e: Exception) {
                // handle
            }
        }
    }
}
