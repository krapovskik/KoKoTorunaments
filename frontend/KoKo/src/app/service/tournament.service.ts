import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TournamentResponse} from "../model/TournamentResponse";
import {Page} from "../model/Page";
import {TournamentList} from "../model/TournamentList";

@Injectable({
    providedIn: 'root'
})
export class TournamentService {

    constructor(private http: HttpClient) {
    }

    findAllTournaments(): Observable<TournamentResponse>{
        return this.http.get<TournamentResponse>('/api/tournament')
    }

    getOngoingTournaments(page: number, size: number): Observable<Page<TournamentList>> {
        return this.http.get<Page<TournamentList>>(`/api/tournament/ongoing?page=${page}&size=${size}`);
    }

    getFinishedTournaments(page: number, size: number): Observable<Page<TournamentList>> {
        return this.http.get<Page<TournamentList>>(`/api/tournament/finished?page=${page}&size=${size}`);
    }

    getComingSoonTournaments(page: number, size: number): Observable<Page<TournamentList>> {
        return this.http.get<Page<TournamentList>>(`/api/tournament/comingSoon?page=${page}&size=${size}`);
    }
}
