package com.example.championship.mappers

import com.example.championship.dto.LeagueDTO
import com.example.championship.models.League
import java.util.ArrayList

class LeagueMapper {
    companion object {
        fun mapLeagueDTOList(leagueDTOList: List<LeagueDTO?>?): List<League> {
            val leagueList: MutableList<League> = ArrayList<League>()
            leagueDTOList?.map { leagueList.add(mapLeagueDTO(it!!)) }
            return leagueList
        }

        private fun mapLeagueDTO(leagueDTO: LeagueDTO): League {
            return League(
                leagueDTO.leagueId,
                leagueDTO.name,
                leagueDTO.sporType,
                leagueDTO.leagueAlternate,
                leagueDTO.logo,
                leagueDTO.badge
            )
        }
    }
}