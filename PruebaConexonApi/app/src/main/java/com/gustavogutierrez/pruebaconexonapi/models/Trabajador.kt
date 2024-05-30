package com.gustavogutierrez.pruebaconexonapi.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trabajador(
    @SerializedName("apellidos")
    val apellidos: String,
    @SerializedName("contraseña")
    val contraseña: String,
    @SerializedName("dni")
    val dni: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("especialidad")
    val especialidad: String,
    @SerializedName("idTrabajador")
    val idTrabajador: Int,
    @SerializedName("nombre")
    val nombre: String
):Parcelable