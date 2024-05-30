package com.gustavogutierrez.pruebaconexonapi.data

import com.gustavogutierrez.pruebaconexonapi.models.Tasks
import com.gustavogutierrez.pruebaconexonapi.models.Trabajador
import kotlinx.coroutines.flow.Flow

class TaskRepository (val dataSource: RemoteDataSource) {
    fun fetchPendingTasks(id: String = "", pass: String = ""): Flow<Tasks> {
        return dataSource.getPendingTasks(id, pass)
    }

    fun fetchFinishedTasks(id: String = "", pass: String = ""): Flow<Tasks> {
        return dataSource.getFinishedTasks(id, pass)
    }

    fun login(email: String = "", pass: String = ""): Flow<Trabajador> {
        return dataSource.login(email, pass)
    }

    fun orderByPriority(id: String = ""): Flow<Tasks> {
        return dataSource.orderByPriority(id)
    }

    fun byPriority(id: String = "", prioridad: String = ""): Flow<Tasks> {
        return dataSource.byPriority(id,prioridad)
    }

    suspend fun finishTask(id: String, tiempo: Double) {
        dataSource.finishTask(id, tiempo)
    }
}