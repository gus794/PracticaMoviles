package com.gustavogutierrez.pruebaconexonapi.data

import com.gustavogutierrez.demo07.data.RemoteDataSource
import com.gustavogutierrez.pruebaconexonapi.models.Tasks
import kotlinx.coroutines.flow.Flow

class TaskRepository (val dataSource: RemoteDataSource) {
    fun fetchPendingTasks(id: String = "", pass: String = ""): Flow<Tasks> {
        return dataSource.getPendingTasks(id, pass)
    }

    fun fetchFinishedTasks(id: String = "", pass: String = ""): Flow<Tasks> {
        return dataSource.getFinishedTasks(id, pass)
    }
}