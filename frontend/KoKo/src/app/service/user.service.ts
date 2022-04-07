import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Response} from "../model/Response";

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
}
