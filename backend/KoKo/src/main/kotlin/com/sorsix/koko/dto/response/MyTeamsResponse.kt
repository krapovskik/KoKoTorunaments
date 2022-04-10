package com.sorsix.koko.dto.response

data class MyTeamsResponse(val teamId: Long, val teamName: String, val teamMemberResponse: List<TeamMemberResponse>)
