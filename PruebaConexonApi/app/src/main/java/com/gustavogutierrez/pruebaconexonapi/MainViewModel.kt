package com.gustavogutierrez.pruebaconexonapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gustavogutierrez.pruebaconexonapi.data.TaskRepository
import com.gustavogutierrez.pruebaconexonapi.models.Tasks
import com.gustavogutierrez.pruebaconexonapi.models.TasksItem
import com.gustavogutierrez.pruebaconexonapi.models.Trabajador
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val tasksRepository: TaskRepository) :
    ViewModel() {
    private var _currentPendingTasks: Flow<Tasks> = tasksRepository.fetchPendingTasks()
    val currentPendingTasks: Flow<Tasks>
        get() = _currentPendingTasks

    private var _currentFinishedtasks: Flow<Tasks> = tasksRepository.fetchFinishedTasks()
    val currentFinishedtasks: Flow<Tasks>
        get() = _currentFinishedtasks

    private var _login: Flow<Trabajador> = tasksRepository.login()
    val login: Flow<Trabajador>
        get() = _login

    private var _currentOrderTasks: Flow<Tasks> = tasksRepository.orderByPriority()
    val currentOrderTasks: Flow<Tasks>
        get() = _currentOrderTasks

    private var _currentByPriority: Flow<Tasks> = tasksRepository.byPriority()
    val currentByPriority: Flow<Tasks>
        get() = _currentByPriority

    fun fetchPendingTasks(id: String = "", pass: String = "") {
        _currentPendingTasks = tasksRepository.fetchPendingTasks(id,pass)
    }

    fun fetchFinishedTasks(id: String = "", pass: String = "") {
        _currentFinishedtasks = tasksRepository.fetchFinishedTasks(id,pass)
    }

    fun orderByPriority(id: String = "") {
        _currentOrderTasks = tasksRepository.orderByPriority(id)
    }

    fun byPriority(id: String = "", prioridad: String = "") {
        _currentByPriority = tasksRepository.byPriority(id,prioridad)
    }

    fun login(email: String = "", pass: String = "") {
        _login = tasksRepository.login(email,pass)
    }

    suspend fun finishTask(id: String, tiempo: Double) {
        tasksRepository.finishTask(id, tiempo)
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val tasksRepository: TaskRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(tasksRepository) as T
        }
    }