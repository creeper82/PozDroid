package com.creeper82.pozdroid.types

data class Vehicle(
    val id: String,

    val airConditioned: Boolean? = null,
    val bike: Boolean? = null,
    val chargers: Boolean? = null,
    val lowFloor: Boolean? = null,
    val lowEntrance: Boolean? = null,
    val ramp: Boolean? = null,
    val ticketMachine: Boolean? = null,
)
