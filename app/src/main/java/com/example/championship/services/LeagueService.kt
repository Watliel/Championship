package com.example.championship.services

import com.example.championship.dto.LeagueListDTO
import retrofit2.Response
import retrofit2.http.*

interface LeagueService {
    @GET("search_all_leagues.php?c=France")
    @Headers("Content-Type: application/json")
    suspend fun getAllLeagues(): Response<LeagueListDTO>

}