package com.gustavogutierrez.demo07.data

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
}
