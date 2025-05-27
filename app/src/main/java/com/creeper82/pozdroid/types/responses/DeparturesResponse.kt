package com.creeper82.pozdroid.types.responses

import com.creeper82.pozdroid.types.Announcement
import com.creeper82.pozdroid.types.Departure

data class DeparturesResponse(
    val bollardName: String,
    val bollardSymbol: String,
    val announcements: Array<Announcement>,
    val departures: Array<Departure>
)
