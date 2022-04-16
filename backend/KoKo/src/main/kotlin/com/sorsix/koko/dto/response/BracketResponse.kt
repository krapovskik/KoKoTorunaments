package com.sorsix.koko.dto.response

data class BracketResponse(val tournament: TournamentResponse, val matches: List<MatchResponse>)
