import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Response} from "../model/Response";
import {MyTeams} from "../model/MyTeams";
import {TeamMember} from "../model/TeamMember";

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

    addUserToTeam(userId: number, teamId: number): Observable<Response<string>> {
        return this.http.post<Response<string>>("/api/team/addUserToTeam", {
            userId: userId,
            teamId: teamId,
        })
    }

    findAllPlayersByTeam(teamId: number): Observable<Response<TeamMember[]>> {
        return this.http.get<Response<TeamMember[]>>(`/api/team/${teamId}/players`)
    }
}