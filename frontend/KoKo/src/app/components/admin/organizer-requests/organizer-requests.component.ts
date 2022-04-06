import {Component, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {finalize} from "rxjs";
import {AdminService} from "../../../service/admin.service";
import {OrganizerRequests} from "../../../model/OrganizerRequests";
import {MessageService} from "../../../service/message.service";
import {PaginationComponent} from "../../pagination/pagination.component";

@Component({
    selector: 'app-organizer-requests',
    templateUrl: './organizer-requests.component.html',
    styleUrls: ['./organizer-requests.component.css']
})
export class OrganizerRequestsComponent {

    @ViewChild(PaginationComponent) pagination!: PaginationComponent;

    navigationPath = "/admin/organizerRequests"
    functionToCall = this.adminService.getOrganizerRequests;
    organizerRequests: OrganizerRequests[] = [];

    constructor(private route: ActivatedRoute, private router: Router, private adminService: AdminService, private messageService: MessageService) {
    }

    onResult(event: OrganizerRequests[]) {
        this.organizerRequests = event
    }

    approveRequest(requestId: number) {
        this.adminService.approveRequest(requestId).pipe(
            finalize(() => {
                this.pagination.newPageEvent();
            })
        ).subscribe({
            next: data => {
                this.messageService.showSuccessMessage(data.response)
            },
            error: err => {
                this.messageService.showErrorMessage(err.error.message)
            }
        })
    }

    rejectRequest(requestId: number) {
        this.adminService.rejectRequest(requestId).pipe(
            finalize(() => {
                this.pagination.newPageEvent();
            })
        ).subscribe({
            next: data => {
                this.messageService.showSuccessMessage(data.response)
            },
            error: err => {
                this.messageService.showErrorMessage(err.error.message)
            }
        })
    }
}
