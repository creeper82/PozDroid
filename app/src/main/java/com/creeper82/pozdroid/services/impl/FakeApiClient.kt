package com.creeper82.pozdroid.services.impl

import com.creeper82.pozdroid.services.abstraction.ApiInterface
import com.creeper82.pozdroid.types.Announcement
import com.creeper82.pozdroid.types.Bollard
import com.creeper82.pozdroid.types.BollardWithDirections
import com.creeper82.pozdroid.types.Departure
import com.creeper82.pozdroid.types.Direction
import com.creeper82.pozdroid.types.DirectionWithStops
import com.creeper82.pozdroid.types.Vehicle
import com.creeper82.pozdroid.types.responses.BollardsResponse
import com.creeper82.pozdroid.types.responses.DeparturesResponse
import com.creeper82.pozdroid.types.responses.LineStopsResponse
import com.creeper82.pozdroid.types.responses.LinesResponse
import com.creeper82.pozdroid.types.responses.StopsResponse
import kotlinx.coroutines.delay
import kotlin.random.Random

class FakeApiClient : ApiInterface {
    private suspend fun randomDelay() {
        delay(Random.nextLong(450, 1300))
    }

    companion object Data {
        val AWF1 = Bollard("AWF", "AWF1")
        val AWF2 = Bollard("AWF", "AWF2")
        val RYWI1 = Bollard("Rynek Wildecki", "RYWI1")
        val RYWI2 = Bollard("Rynek Wildecki", "RYWI2")
        val BIPRK1 = Bollard("Biedrusko/Park", "BIPRK1")
        val BIPRK2 = Bollard("Biedrusko/Park", "BIPRK2")

        val bollards = arrayOf(AWF1, AWF2, RYWI1, RYWI2, BIPRK1, BIPRK2)

        val stops = arrayOf(
            Bollard("AWF", "AWF"),
            Bollard("Rynek Wildecki", "RYWI"),
            Bollard("Biedrusko/Park", "BIPRK")
        )

        val directions: Array<Direction> = arrayOf(
            Direction("1", "Franowo"),
            Direction("3", "Unii Lubelskiej"),
            Direction("171", "Gorzów Wielkopolski")
        )

        val announcements: Array<Announcement> = arrayOf(
            Announcement(
                "Tekst przykładowego ogłoszenia",
                "01.01.2030",
                "31.12.2030"
            )
        )

        fun createFakeDepartures(symbol: String): DeparturesResponse {
            return DeparturesResponse(
                bollardName = bollards.find { b -> b.symbol == symbol }?.name ?: "Name",
                bollardSymbol = symbol,
                announcements = if (Random.nextBoolean()) announcements else emptyArray(),
                departures = departures
            )
        }

        val departures: Array<Departure> = arrayOf(
            Departure(
                line = "1",
                direction = "Franowo",
                departure = "11:03",
                minutes = 3,
                realTime = true,
                onStopPoint = false
            ),
            Departure(
                line = "171",
                direction = "Gorzów Wielkopolski",
                departure = "11:20",
                minutes = 20,
                realTime = true,
                onStopPoint = false,
                vehicle = Vehicle(
                    id = "1031",
                    airConditioned = true,
                    bike = false,
                    chargers = false,
                    lowFloor = true,
                    lowEntrance = true,
                    ramp = true,
                    ticketMachine = false
                )
            ),
            Departure(
                line = "3",
                direction = "Unii Lubelskiej",
                departure = "11:21",
                minutes = 21,
                realTime = true,
                onStopPoint = false,
                vehicle = Vehicle(
                    id = "601",
                    airConditioned = true,
                    bike = true,
                    chargers = true,
                    lowFloor = false,
                    lowEntrance = true,
                    ramp = true
                )
            ),
            Departure(
                line = "1",
                direction = "Franowo",
                departure = "12:01",
                minutes = 61,
                realTime = false,
                onStopPoint = false
            )
        )

        val lineDirections: Array<DirectionWithStops> = arrayOf(
            DirectionWithStops(
                direction = "Biedrusko/Park",
                bollards = bollards
            ),
            DirectionWithStops(
                direction = "AWF",
                bollards = bollards.reversed().toTypedArray()
            )
        )
    }

    override suspend fun getStops(keyword: String): StopsResponse {
        randomDelay()
        return stops.filter { it.name.startsWith(keyword) }.toTypedArray()
    }

    override suspend fun getBollards(name: String): BollardsResponse {
        randomDelay()
        return bollards
            .filter { b -> b.name == name }
            .map { b -> BollardWithDirections(b.name, b.symbol, directions) }
            .toTypedArray()
    }

    override suspend fun getDepartures(symbol: String): DeparturesResponse {
        randomDelay()
        return createFakeDepartures(symbol)
    }

    override suspend fun getLines(keyword: String): LinesResponse {
        randomDelay()
        return arrayOf("1", "3", "10", "171", "PKM1")
            .filter { l -> l.startsWith(keyword) }
            .toTypedArray()
    }

    override suspend fun getLine(line: String): LineStopsResponse {
        randomDelay()
        return lineDirections
    }
}