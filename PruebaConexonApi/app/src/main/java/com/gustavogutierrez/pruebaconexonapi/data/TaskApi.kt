package com.gustavogutierrez.pruebaconexonapi.data

import com.gustavogutierrez.pruebaconexonapi.models.Tasks
import com.gustavogutierrez.pruebaconexonapi.models.TasksItem
import com.gustavogutierrez.pruebaconexonapi.models.Trabajador
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// Esta clase proporciona la configuración de la API Retrofit para acceder a los datos de las palabras
class TaskApi {
    companion object {
        const val BASE_URL = "http://192.168.1.58:8080/api/"

        fun getRetrofit2Api(): Retrofit2ApiInterface {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(Retrofit2ApiInterface::class.java)
        }
    }
}

// Interfaz que define los métodos de la API Retrofit para acceder a los datos de las palabras
interface Retrofit2ApiInterface {
    // Método para obtener las tareas pendientes desde la API
    @GET("trabajosPending/{id}/{contrasenya}")
    suspend fun getPendingTasks(@Path("id") id: String = "", @Path("contrasenya") contraseña: String = ""): Tasks

    // Método para obtener las tareas terminadas desde la API
    @GET("trabajosFinished/{id}/{contrasenya}")
    suspend fun getFinishedTasks(@Path("id") id: String = "", @Path("contrasenya") contraseña: String = ""): Tasks

    @GET("trabajadores/{email}/{contrasenya}")
    suspend fun login(@Path("email") email: String = "", @Path("contrasenya") contraseña: String = ""): Trabajador

    @GET("trabajos/orderedByPriority/{idTrabajador}")
    suspend fun orderByPriority(@Path("idTrabajador") id: String = ""): Tasks

    @GET("trabajos/ByPriority/{idTrabajador}/{prioridad}")
    suspend fun byPriority(@Path("idTrabajador") id: String = "", @Path("prioridad") prioridad: String = ""): Tasks

    @PUT("trabajosFinish/{id}")
    suspend fun finishTask(@Path("id") id: String = "",@Query("tiempo") tiempo: Double = 0.0): Tasks
}
