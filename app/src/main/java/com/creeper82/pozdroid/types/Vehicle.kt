package com.creeper82.pozdroid.types

data class Vehicle(
    val id: String,

    val airConditioned: Boolean?,
    val bike: Boolean?,
    val chargers: Boolean?,
    val lowFloor: Boolean?,
    val lowEntrance: Boolean?,
    val ramp: Boolean?,
    val ticketMachine: Boolean?,
)
