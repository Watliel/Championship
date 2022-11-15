package com.example.championship.services

import com.example.championship.dto.TeamListDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TeamService {
    @GET("search_all_teams.php")
    @Headers("Content-Type: application/json")
    suspend fun getTeamsByLeagueName(@Query("l") leagueName: String): Response<TeamListDTO>

    @GET("searchteams.php")
    @Headers("Content-Type: application/json")
    suspend fun geTeamDetail(@Query("t") teamName: String): Response<TeamListDTO>

}