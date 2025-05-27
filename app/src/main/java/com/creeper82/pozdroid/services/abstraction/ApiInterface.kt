package com.creeper82.pozdroid.services.abstraction

import com.creeper82.pozdroid.types.responses.BollardsResponse
import com.creeper82.pozdroid.types.responses.DeparturesResponse
import com.creeper82.pozdroid.types.responses.LineStopsResponse
import com.creeper82.pozdroid.types.responses.LinesResponse
import com.creeper82.pozdroid.types.responses.StopsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("stops")
    suspend fun getStops(@Query("q") keyword: String): StopsResponse

    @GET("bollards")
    suspend fun getBollards(@Query("name") name: String): BollardsResponse

    @GET("departures")
    suspend fun getDepartures(@Query("bollard_symbol") symbol: String): DeparturesResponse

    @GET("lines")
    suspend fun getLines(@Query("q") keyword: String): LinesResponse

    @GET("line")
    suspend fun getLine(@Query("name") line: String): LineStopsResponse
}