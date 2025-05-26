package com.creeper82.pozdroid.types

data class Departure(
    val line: String,
    val direction: String,
    val departure: String,
    val minutes: Int,
    val realTime: Boolean,
    val onStopPoint: Boolean,
    val vehicle: Vehicle?
)
