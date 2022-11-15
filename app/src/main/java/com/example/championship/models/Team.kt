package com.example.championship.models

data class Team(
    val name: String?,
    val banner: String?,
    val description: String?,
    val championship: String?,
    val logo: String?,
    val badge: String?,
    val teamId: String?
)

object oneTeam {

    init {
    }
    lateinit var team: Team

}