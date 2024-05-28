package com.gustavogutierrez.pruebaconexonapi.models


import com.google.gson.annotations.SerializedName

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
)