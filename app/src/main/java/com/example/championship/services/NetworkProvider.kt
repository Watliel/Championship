package com.example.championship.services

import com.example.championship.models.League
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkProvider {

    private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/50130162/"
    var leagueService: LeagueService

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private lateinit var instance: NetworkProvider


    init {
        leagueService = retrofit.create(LeagueService::class.java)
    }

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
/*
    suspend fun getLeague(): Flow<DataState<List<League>>> = flow {
        val alLeagues = leagueService.getAllLeagues()
        val leagues = leagueMapper.mapFromEntityList(alLeagues)
        emit(DataState.Success(cacheMapper.mapFromEntityList(leagues)))
    } catch (e: Exception) {
        emit(DataState.Error(e))
    }
    */
}