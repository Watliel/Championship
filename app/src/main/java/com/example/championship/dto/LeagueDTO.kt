package com.example.championship.dto

import com.google.gson.annotations.SerializedName

data class LeagueDTO(
    @SerializedName("idLeague") var leagueId: String,
    @SerializedName("strLeague") var name: String,
    @SerializedName("strSport") var sporType: String,
    @SerializedName("strLeagueAlternate") var leagueAlternate: String?,
    @SerializedName("strLogo") val logo: String?,
    @SerializedName("strBadge") val badge: String?,
    )
