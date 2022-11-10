package com.example.championship.models

data class League(
    val leagueId: String,
    var name: String,
    var sportType: String,
    var leagueAlternate: String?,
    var logo: String?,
    var badge: String?
)
