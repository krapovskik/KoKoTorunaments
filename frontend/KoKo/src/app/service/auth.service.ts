import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/User";
import {Response} from "../model/Response";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private http: HttpClient) {
    }

    login(username: string, password: string): Observable<User> {
        return this.http.post<User>('/api/auth/login', {
            username: username,
            password: password
        });
    }

    activateAccount(token: string, firstName: string, lastName: string, password: string): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/auth/activate', {
            token: token,
            firstName: firstName,
            lastName: lastName,
            password: password
        });
    }

    register(email: string): Observable<Response<string>> {
        return this.http.post<Response<string>>('/api/auth/register', {
            email: email
        });
    }
}
