import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TournamentResponse} from "../model/TournamentResponse";
import {Page} from "../model/Page";
import {Tournament} from "../model/Tournament";
import {Response} from "../model/Response";
import {Bracket} from "../model/Bracket";
import {ProfileStatistics} from "../model/ProfileStatistics";

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

    getTournamentBracket(tournamentId: number): Observable<Response<Bracket>> {
        return this.http.get<Response<Bracket>>(`/api/tournament/bracket/${tournamentId}`)
    }

    addTeamToTournament(teamId: number, tournamentId: number): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/tournament/addTeam', {teamId, tournamentId})
    }

    addPlayerToTournament(tournamentId: number): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/tournament/addPlayer', {tournamentId})
    }

    getAllTournamentsByUser(appUserId: number): ((page: number, size: number) => Observable<Page<Tournament>>) {
        return (page, size) => {
            return this.http.get<Page<Tournament>>(`/api/tournament/profile/${appUserId}/all?page=${page}&size=${size}`)
        }
    }

    getWonTournamentsByUser(appUserId: number): ((page: number, size: number) => Observable<Page<Tournament>>) {
        return (page, size) => {
            return this.http.get<Page<Tournament>>(`/api/tournament/profile/${appUserId}/won?page=${page}&size=${size}`)
        }
    }

    getProfileStatistics(appUserId: number): Observable<Response<ProfileStatistics>> {
        return this.http.get<Response<ProfileStatistics>>(`/api/tournament/profile/${appUserId}/statistics`)
    }

    editMatch(
        tournamentId: number,
        matchId: number,
        score1: number,
        score2: number,
        isFinished: boolean,
        winner: number
    ): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/tournament/editMatch', {
            tournamentId: tournamentId,
            matchId: matchId,
            score1: score1,
            score2: score2,
            isFinished: isFinished,
            winner: winner
        })
    }

    getEmptyBracket(size: number) {

        let matches = []
        let round = 0;
        while (size > 0) {
            for (let i = 0; i < size; i++) {
                matches.push({
                    id: 0,
                    number: 0,
                    stage_id: 0,
                    group_id: 0,
                    round_id: round,
                    child_count: 0,
                    status: 4,
                    opponent1: '',
                    opponent2: '',
                })
            }
            size = Math.floor(size / 2);
            round++;
        }

        return {
            stages: [
                {
                    id: 0,
                    tournament_id: 0,
                    name: 'Tournament hasn\'t started yet',
                    type: 'single_elimination',
                    number: 1,
                    settings: {},
                },
            ],
            matches: matches,
            matchGames: [],
            participants: [],
        };
    }

    getTournamentTypes(): Observable<string[]> {
        return this.http.get<string[]>('/api/tournament/tournamentTypes')
    }

    createTournament(tournamentName: string,
                     tournamentCategory: string,
                     tournamentLocation: string,
                     tournamentDescription: string,
                     numberOfParticipants: number,
                     tournamentType: string,
                     tournamentDate: string,
                     tournamentTime: string): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/tournament/createTournament', {
            tournamentName,
            tournamentCategory,
            tournamentLocation,
            tournamentDescription,
            numberOfParticipants,
            tournamentType,
            tournamentDate,
            tournamentTime
        })
    }

}
