package com.example.championship.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.championship.mappers.LeagueMapper
import com.example.championship.models.League
import com.example.championship.services.LeagueService
import com.example.championship.services.NetworkProvider
import kotlinx.coroutines.*

class LeagueListViewModel: ViewModel() {
    var allLeagues = MutableLiveData<List<League>>()
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

    private fun onError(message: String) {
        leagueLoadError.value = true
        loading.value = false
        Log.d("onError LeagueViewModel", message)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}