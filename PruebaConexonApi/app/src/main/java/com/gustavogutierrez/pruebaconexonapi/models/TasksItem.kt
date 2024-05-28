package com.gustavogutierrez.pruebaconexonapi.models


import com.google.gson.annotations.SerializedName

data class TasksItem(
    @SerializedName("categoria")
    val categoria: String,
    @SerializedName("codTrabajo")
    val codTrabajo: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("fechaFin")
    val fechaFin: Any?,
    @SerializedName("fechaInicio")
    val fechaInicio: String,
    @SerializedName("prioridad")
    val prioridad: Int,
    @SerializedName("tiempo")
    val tiempo: Double,
    @SerializedName("trabajador")
    val trabajador: Trabajador?
)