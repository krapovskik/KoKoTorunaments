import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Response} from "../model/Response";

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
}
