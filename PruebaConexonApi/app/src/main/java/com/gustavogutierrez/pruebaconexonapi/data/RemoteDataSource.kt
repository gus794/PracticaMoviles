package com.gustavogutierrez.pruebaconexonapi.data

import android.util.Log
import com.gustavogutierrez.pruebaconexonapi.data.TaskApi
import kotlinx.coroutines.flow.flow

// Esta clase representa la base de datos remota de las palabras
class RemoteDataSource {
    private val api = TaskApi.getRetrofit2Api()

    fun getPendingTasks(id: String = "", pass: String = "") = flow {
        Log.i("TAG", "getPendingTasks: "+api.getPendingTasks(id, pass))
        emit(api.getPendingTasks(id, pass))
    }

    fun getFinishedTasks(id: String = "", pass: String = "") = flow {
        emit(api.getFinishedTasks(id, pass))
    }

    fun login(email: String = "", pass: String = "") = flow {
        emit(api.login(email, pass))
    }

    fun orderByPriority(id: String = "") = flow {
        emit(api.orderByPriority(id))
    }

    fun byPriority(id: String = "", prioridad: String = "") = flow {
        emit(api.byPriority(id,prioridad))
    }

    fun finishTask(id: String = "", tiempo: Double = 0.0) = flow {
        emit(api.finishTask(id, tiempo))
    }
}
