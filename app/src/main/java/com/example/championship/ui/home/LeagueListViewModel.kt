package com.example.championship.ui.home
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.championship.mappers.LeagueMapper
import com.example.championship.mappers.TeamMapper
import com.example.championship.models.League
import com.example.championship.models.Team
import com.example.championship.models.OneTeam
import com.example.championship.services.LeagueService
import com.example.championship.services.NetworkProvider
import com.example.championship.services.TeamService
import kotlinx.coroutines.*
import java.util.ArrayList

class LeagueListViewModel: ViewModel() {
    var allLeagues = MutableLiveData<List<League>>()
    var teamsInLeague = MutableLiveData<List<Team>>()
    var leagueFiltered: MutableList<League?> = ArrayList()
    var suggestionLeagueList:  MutableList<String> = ArrayList<String>()


    var loading = MutableLiveData<Boolean>()
    var leagueLoadError = MutableLiveData<Boolean>()
    var teamDetailLoaded = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError("Exception ${throwable.localizedMessage}") }
    private var job: Job? = null

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
                    Log.d("ERROR - LeagueList", res.message())
                }
            }
        }
    }

    fun geTeamsByLeagueNames(name: String) {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val res = NetworkProvider.buildService(TeamService::class.java).getTeamsByLeagueName(name)
            withContext(Dispatchers.Main) {
                loading.value = false
                if (res.isSuccessful && res.body()?.teams != null) {
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
                    if (team != null) {
                        OneTeam.team = TeamMapper.mapTeamDTO(team)
                        teamDetailLoaded.value = true
                    } else onError("Team_detail null")

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