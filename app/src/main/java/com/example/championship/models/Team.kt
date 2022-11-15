package com.example.championship.models

data class Team(
    val name: String?,
    val banner: String?,
    val description: String?,
    val league: String?,
    val logo: String?,
    val badge: String?,
    val country: String?,
    val teamId: String?
)

object OneTeam {

    init {
    }
    lateinit var team: Team

}