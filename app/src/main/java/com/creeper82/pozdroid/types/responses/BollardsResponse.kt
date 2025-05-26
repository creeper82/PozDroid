package com.creeper82.pozdroid.types.responses

import com.creeper82.pozdroid.types.Direction

data class BollardsResponse(
    val name: String,
    val symbol: String,
    val directions: Array<Direction>
)
