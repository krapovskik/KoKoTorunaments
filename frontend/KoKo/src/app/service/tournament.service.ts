import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TournamentResponse} from "../model/TournamentResponse";
import {Page} from "../model/Page";
import {Tournament} from "../model/Tournament";
import {Response} from "../model/Response";

@Injectable({
    providedIn: 'root'
})
export class TournamentService {

    constructor(private http: HttpClient) {
    }

    findAllTournaments(): Observable<TournamentResponse> {
        return this.http.get<TournamentResponse>('/api/tournament')
    }

    getOngoingTournaments(page: number, size: number): Observable<Page<Tournament>> {
        return this.http.get<Page<Tournament>>(`/api/tournament/ongoing?page=${page}&size=${size}`);
    }

    getFinishedTournaments(page: number, size: number): Observable<Page<Tournament>> {
        return this.http.get<Page<Tournament>>(`/api/tournament/finished?page=${page}&size=${size}`);
    }

    getComingSoonTournaments(page: number, size: number): Observable<Page<Tournament>> {
        return this.http.get<Page<Tournament>>(`/api/tournament/comingSoon?page=${page}&size=${size}`);
    }

    getTournamentBracket(tournamentId: number) {
        return {
            stages: [
                {
                    id: 0,
                    tournament_id: 0,
                    name: 'Tournament name',
                    type: 'single_elimination',
                    number: 1,
                    settings: {
                        size: 16,
                        grandFinal: 'single',
                        matchesChildCount: 0,
                    },
                },
            ],
            matches: [
                {
                    id: 0,
                    number: 1,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 0,
                    child_count: 0,
                    status: 4,
                    opponent1: {
                        id: 0,
                        position: 1,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 1,
                        position: 2,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 1,
                    number: 2,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 0,
                    child_count: 0,
                    status: 3,
                    opponent1: {
                        id: 2,
                        position: 3,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 3,
                        position: 4,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 2,
                    number: 3,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 0,
                    child_count: 0,
                    status: 0,
                    opponent1: {
                        id: 4,
                        position: 5,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 5,
                        position: 6,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 3,
                    number: 4,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 0,
                    child_count: 0,
                    status: 2,
                    opponent1: {
                        id: 6,
                        position: 7,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 7,
                        position: 8,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 4,
                    number: 5,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 0,
                    child_count: 0,
                    status: 4,
                    opponent1: {
                        id: 8,
                        position: 9,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 9,
                        position: 10,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 5,
                    number: 6,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 0,
                    child_count: 0,
                    status: 3,
                    opponent1: {
                        id: 10,
                        position: 11,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 11,
                        position: 12,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 6,
                    number: 7,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 0,
                    child_count: 0,
                    status: 0,
                    opponent1: {
                        id: 12,
                        position: 13,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 13,
                        position: 14,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 7,
                    number: 8,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 0,
                    child_count: 0,
                    status: 2,
                    opponent1: {
                        id: 14,
                        position: 15,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 15,
                        position: 16,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 8,
                    number: 1,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 1,
                    child_count: 0,
                    status: 1,
                    opponent1: {
                        id: 0,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 2,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 8,
                    number: 2,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 1,
                    child_count: 0,
                    status: 1,
                    opponent1: {
                        id: 4,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 6,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 8,
                    number: 3,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 1,
                    child_count: 0,
                    status: 1,
                    opponent1: {
                        id: 8,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 10,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 5,
                    number: 4,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 1,
                    child_count: 0,
                    status: 1,
                    opponent1: {
                        id: 12,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 14,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 6,
                    number: 0,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 2,
                    child_count: 0,
                    status: 0,
                    opponent1: {
                        id: 0,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 4,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 8,
                    number: 1,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 2,
                    child_count: 0,
                    status: 1,
                    opponent1: {
                        id: 8,
                        score: 'W',
                        result: 'win',
                    },
                    opponent2: {
                        id: 12,
                        score: 'L',
                        result: 'loss',
                    },
                },
                {
                    id: 8,
                    number: 0,
                    stage_id: 0,
                    group_id: 0,
                    round_id: 3,
                    child_count: 0,
                    status: 1,
                    opponent1: {
                        id: 0,
                        score: 8,
                        result: 'win',
                    },
                    opponent2: {
                        id: 8,
                        score: 5,
                        result: 'loss',
                    },
                },
            ],
            matchGames: [],
            participants: [
                {
                    id: 0,
                    tournament_id: 0,
                    name: 'Team 1',
                },
                {
                    id: 1,
                    tournament_id: 0,
                    name: 'Team 2',
                },
                {
                    id: 2,
                    tournament_id: 0,
                    name: 'Team 3',
                },
                {
                    id: 3,
                    tournament_id: 0,
                    name: 'Team 4',
                },
                {
                    id: 4,
                    tournament_id: 0,
                    name: 'Team 5',
                },
                {
                    id: 5,
                    tournament_id: 0,
                    name: 'Team 6',
                },
                {
                    id: 6,
                    tournament_id: 0,
                    name: 'Team 7',
                },
                {
                    id: 7,
                    tournament_id: 0,
                    name: 'Team 8',
                },
                {
                    id: 8,
                    tournament_id: 0,
                    name: 'Team 9',
                },
                {
                    id: 9,
                    tournament_id: 0,
                    name: 'Team 10',
                },
                {
                    id: 10,
                    tournament_id: 0,
                    name: 'Team 11',
                },
                {
                    id: 11,
                    tournament_id: 0,
                    name: 'Team 12',
                },
                {
                    id: 12,
                    tournament_id: 0,
                    name: 'Team 13',
                },
                {
                    id: 13,
                    tournament_id: 0,
                    name: 'Team 14',
                },
                {
                    id: 14,
                    tournament_id: 0,
                    name: 'Team 15',
                },
                {
                    id: 15,
                    tournament_id: 0,
                    name: 'Team 16',
                },
            ],
        };
    }

    addTeamToTournament(teamId: number, tournamentId: number): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/tournament/addTeam', {teamId, tournamentId})
    }

    addPlayerToTournament(appUserId: number, tournamentId: number): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/tournament/addPlayer', {appUserId, tournamentId})
    }

}
