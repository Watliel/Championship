package com.example.championship.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.championship.dto.LeagueDTO
import com.example.championship.mappers.LeagueMapper
import com.example.championship.models.League
import com.example.championship.services.LeagueService
import com.example.championship.services.NetworkProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class LeagueListViewModel: ViewModel() {
    var allLeagues = MutableLiveData<List<League>>()
    var loading = MutableLiveData<Boolean>()
    var leagueLoadError = MutableLiveData<Boolean>()


    fun getLeagues() {

        viewModelScope.launch {

        }

        CoroutineScope(Dispatchers.IO).launch {
            val res = NetworkProvider.buildService(LeagueService::class.java).getAllLeagues()
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        Log.d("TEST", res.body()?.leagues.toString())
                        allLeagues.value = LeagueMapper.mapLeagueDTOList(res.body()?.leagues)
                        Log.d("TEST", allLeagues.value.toString())

                    } catch (e: IOException) {
                        Log.d("ERROR", e.toString())
                    }
                } else {
                    Log.d("ERROR", res.message())
                }
            }
        }
    }
}