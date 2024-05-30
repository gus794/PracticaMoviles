package com.gustavogutierrez.pruebaconexonapi.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TasksItem(
    @SerializedName("categoria")
    val categoria: String,
    @SerializedName("codTrabajo")
    val codTrabajo: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("fechaFin")
    val fechaFin: String?,
    @SerializedName("fechaInicio")
    val fechaInicio: String,
    @SerializedName("prioridad")
    val prioridad: Int,
    @SerializedName("tiempo")
    val tiempo: Double,
    @SerializedName("trabajador")
    val trabajador: Trabajador?
):Parcelable