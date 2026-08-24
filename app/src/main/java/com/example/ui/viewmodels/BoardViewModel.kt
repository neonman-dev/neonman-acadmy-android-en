package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BoardViewModel : ViewModel() {

    private val _boards = MutableStateFlow<List<BoardItem>>(emptyList())
    val boards: StateFlow<List<BoardItem>> = _boards.asStateFlow()

    private val _selectedBoard = MutableStateFlow<BoardDetail?>(null)
    val selectedBoard: StateFlow<BoardDetail?> = _selectedBoard.asStateFlow()

    private val _currentCanvasPaths = MutableStateFlow<List<DrawPathData>>(emptyList())
    val currentCanvasPaths: StateFlow<List<DrawPathData>> = _currentCanvasPaths.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadBoards(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = ApiClient.service.getBoards(token)
                _boards.value = res.boards
            } catch (e: Exception) {
                _boards.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBoardDetail(token: String, id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val wrapper = ApiClient.service.getBoardDetail(id, token)
                _selectedBoard.value = wrapper.board
                _currentCanvasPaths.value = wrapper.board.canvas
            } catch (e: Exception) {
                _selectedBoard.value = null
                _currentCanvasPaths.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createBoard(token: String, title: String, onDone: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val res = ApiClient.service.createBoard(CreateBoardRequest(token, title))
                loadBoards(token)
                onDone(res.board.id)
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun updatePaths(paths: List<DrawPathData>) {
        _currentCanvasPaths.value = paths
    }

    fun saveBoardCanvas(boardId: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            try {
                ApiClient.service.updateBoard(boardId, UpdateBoardRequest(_currentCanvasPaths.value))
                onSaved()
            } catch (e: Exception) {
                // handle error
            }
        }
    }
}
