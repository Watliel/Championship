package com.example.championship.dto

import com.google.gson.annotations.SerializedName

data class TeamDTO(
    @SerializedName("idTeam") var teamId: String,
    @SerializedName("strTeam") var name: String,
    @SerializedName("strTeamLogo") var logo: String,
    @SerializedName("strTeamBadge") var badge: String,
    @SerializedName("strDescriptionEN") var description: String?,
    @SerializedName("strTeamBanner") var banner: String?,
    @SerializedName("strChampionShip") var championship: String?

)