import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Page} from "../model/Page";
import {Observable, of} from "rxjs";
import {OrganizerRequests} from "../model/OrganizerRequests";
import {Response} from "../model/Response";
import {Organizer} from "../model/Organizer";

@Injectable({
    providedIn: 'root'
})
export class AdminService {

    constructor(private http: HttpClient) {
    }

    getOrganizerRequests(page: number, size: number): Observable<Page<OrganizerRequests>> {
        return this.http.get<Page<OrganizerRequests>>(`/api/admin/organizerRequests?page=${page}&size=${size}`);
    }

    getOrganizers(page: number, size: number, http: HttpClient = this.http): Observable<Page<Organizer>> {
        return http.get<Page<Organizer>>(`/api/admin/organizers?page=${page}&size=${size}`);
    }

    approveRequest(requestId: number): Observable<Response<string>> {
        return this.http.put<Response<string>>('/api/admin/approveRequest', requestId)
    }

    rejectRequest(requestId: number) {
        return this.http.put<Response<string>>('/api/admin/rejectRequest', requestId)
    }

    revokeOrganizer(userId: number) {
        return this.http.put<Response<string>>('/api/admin/revokeOrganizer', userId)
    }
}
