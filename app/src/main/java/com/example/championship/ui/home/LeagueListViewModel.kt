package com.example.championship.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.championship.mappers.LeagueMapper
import com.example.championship.mappers.TeamMapper
import com.example.championship.models.League
import com.example.championship.models.Team
import com.example.championship.models.oneTeam
import com.example.championship.services.LeagueService
import com.example.championship.services.NetworkProvider
import com.example.championship.services.TeamService
import kotlinx.coroutines.*

class LeagueListViewModel: ViewModel() {
    var allLeagues = MutableLiveData<List<League>>()
    var teamsInLeague = MutableLiveData<List<Team>>()

    var loading = MutableLiveData<Boolean>()
    var leagueLoadError = MutableLiveData<Boolean>()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable -> onError("Exception ${throwable.localizedMessage}") }
    var job: Job? = null

    fun getLeagues() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val res = NetworkProvider.buildService(LeagueService::class.java).getAllLeagues()
            withContext(Dispatchers.Main) {
                loading.value = false
                if (res.isSuccessful) {
                    allLeagues.value = LeagueMapper.mapLeagueDTOList(res.body()?.leagues)
                } else {
                    onError("Error: ${res.message()}")
                    Log.d("ERROR - LieagueList", res.message())
                }
            }
        }
    }

    fun geTeamsLeaguesByNames(name: String) {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val res = NetworkProvider.buildService(TeamService::class.java).getTeamsByLeagueName(name)
            withContext(Dispatchers.Main) {
                loading.value = false
                if (res.isSuccessful) {
                    val teams = TeamMapper.mapTeamDTOList(res.body()?.teams)
                   teamsInLeague.value = teams.filterIndexed { index, _ -> index % 2 == 0 }.sortedByDescending { it.name }
                } else {
                    onError("Error: ${res.message()}")
                    Log.d("ERROR - LeagueList", res.message())
                }
            }
        }
    }

    fun geTeamDetail(name: String) {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val res = NetworkProvider.buildService(TeamService::class.java).geTeamDetail(name)
            withContext(Dispatchers.Main) {
                loading.value = false
                if (res.isSuccessful) {
                    val team = res.body()?.teams?.get(0)
                    oneTeam.team = TeamMapper.mapTeamDTO(team)
                    Log.d("DETAIL", oneTeam.team.toString())
                } else {
                    onError("Error: ${res.message()}")
                    Log.d("ERROR - geTeamDetail", res.message())
                }
            }
        }
    }

    private fun onError(message: String) {
        leagueLoadError.postValue(true)
        loading.postValue(false)
        Log.d("onError LeagueViewModel", message)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}