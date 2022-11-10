package com.example.championship.dto

import com.google.gson.annotations.SerializedName

data class LeagueListDTO (
    @SerializedName("countries") var leagues: List<LeagueDTO>?,
    )
