package com.example.championship.dto

import com.google.gson.annotations.SerializedName

data class TeamListDTO (
    @SerializedName("teams") var teams: List<TeamDTO>?,
    )