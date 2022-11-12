package com.example.championship.dto

import com.google.gson.annotations.SerializedName

data class TeamDTO(
    @SerializedName("idTeam") var teamId: String,
    @SerializedName("strTeam") var name: String
)