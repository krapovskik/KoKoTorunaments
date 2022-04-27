import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Response} from "../model/Response";
import {Player} from "../model/Player";

@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor(private http: HttpClient) {
    }

    sendOrganizerRequest(title: string, description: string): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/user/requestOrganizer', {
            title: title,
            description: description
        });
    }

    searchUser(query: string): Observable<Player[]> {
        return this.http.get<Player[]>(`/api/user/searchUser?query=${query}`)
    }

    sendInvite(email: string, teamId: number): Observable<Response<string>> {
        return this.http.post<Response<string>>("/api/user/sendInvite", {
            email: email,
            teamId: teamId
        })
    }

    updateProfilePhoto(image: string): Observable<Response<string>> {
        return this.http.put<Response<string>>("/api/user/changeProfilePhoto", image)
    }
}
