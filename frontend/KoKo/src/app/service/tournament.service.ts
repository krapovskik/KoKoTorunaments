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

    addTeamToTournament(teamId: number, tournamentId: number): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/tournament/addTeam', {teamId, tournamentId})
    }
}
