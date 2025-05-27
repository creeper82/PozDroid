package com.creeper82.pozdroid.types.responses

import com.creeper82.pozdroid.types.Bollard

data class LineStopsResponse(
    val direction: String,
    val bollards: Array<Bollard>
)
