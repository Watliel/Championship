package com.example.championship.mappers

import com.example.championship.dto.LeagueDTO
import com.example.championship.dto.TeamDTO
import com.example.championship.models.Team
import java.util.ArrayList

class TeamMapper {
    companion object {
        fun mapTeamDTOList(teamDTOList: List<TeamDTO?>?): List<Team> {
            val teamList: MutableList<Team> = ArrayList<Team>()
            teamDTOList?.map { teamList.add(mapTeamDTO(it!!)) }
            return teamList
        }

        private fun mapTeamDTO(teamDTO: TeamDTO): Team {
            return Team(
                teamDTO.name,
                teamDTO.logo,
                teamDTO.badge,
                teamDTO.teamId,
            )
        }
    }
}