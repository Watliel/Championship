package com.example.championship.services

import com.example.championship.models.League
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkProvider {

    const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/50130162/"
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
    suspend fun getBlog(): Flow<DataState<List<League>>> = flow {
        val networkBlogs = leagueService.getAllLeagues()
        val blogs = blogMapper.mapFromEntityList(networkBlogs)
        for (blog in blogs) {
            blogDao.insert(cacheMapper.mapToEntity(blog))
        }
        val cachedBlogs = blogDao.get()
        emit(DataState.Success(cacheMapper.mapFromEntityList(cachedBlogs)))
    } catch (e: Exception) {
        emit(DataState.Error(e))
    }
    */
}