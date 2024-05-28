package com.gustavogutierrez.pruebaconexonapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gustavogutierrez.pruebaconexonapi.data.TaskRepository
import com.gustavogutierrez.pruebaconexonapi.models.Tasks
import com.gustavogutierrez.pruebaconexonapi.models.TasksItem
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val tasksRepository: TaskRepository) :
    ViewModel() {
    private var _currentPendingTasks: Flow<Tasks> = tasksRepository.fetchPendingTasks()
    val currentPendingTasks: Flow<Tasks>
        get() = _currentPendingTasks

    private var _currentFinishedtasks: Flow<Tasks> = tasksRepository.fetchFinishedTasks()
    val currentFinishedtasks: Flow<Tasks>
        get() = _currentFinishedtasks

    fun fetchPendingTasks(id: String = "", pass: String = "") {
        _currentPendingTasks = tasksRepository.fetchPendingTasks(id,pass)
    }

    fun fetchFinishedTasks(id: String = "", pass: String = "") {
        _currentFinishedtasks = tasksRepository.fetchFinishedTasks(id,pass)
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val tasksRepository: TaskRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(tasksRepository) as T
        }
    }