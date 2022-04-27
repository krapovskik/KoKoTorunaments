import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Response} from "../model/Response";
import {MyTeams} from "../model/MyTeams";
import {Player} from "../model/Player";
import {Team} from "../model/Team";

@Injectable({
    providedIn: 'root'
})
export class TeamService {

    constructor(private http: HttpClient) {
    }

    createTeam(teamName: string): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/team/create', {
            teamName: teamName
        })
    }

    getMyTeams(): Observable<MyTeams[]> {
        return this.http.get<MyTeams[]>("/api/team/myTeams");
    }

    getTeamsForUser(): Observable<Team[]>{
        return this.http.get<Team[]>("/api/team/userTeams");
    }

    addUserToTeam(userId: number, teamId: number): Observable<Response<string>> {
        return this.http.post<Response<string>>("/api/team/addUserToTeam", {
            userId: userId,
            teamId: teamId,
        })
    }

    findAllPlayersByTeam(teamId: number): Observable<Response<Player[]>> {
        return this.http.get<Response<Player[]>>(`/api/team/${teamId}/players`)
    }
}
