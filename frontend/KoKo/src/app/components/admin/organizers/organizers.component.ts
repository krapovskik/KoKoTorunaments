import {Component, ViewChild} from '@angular/core';
import {finalize} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {AdminService} from "../../../service/admin.service";
import {MessageService} from "../../../service/message.service";
import {Organizer} from "../../../model/Organizer";
import {PaginationComponent} from "../../pagination/pagination.component";

@Component({
    selector: 'app-organizers',
    templateUrl: './organizers.component.html',
    styleUrls: ['./organizers.component.css']
})
export class OrganizersComponent {

    @ViewChild(PaginationComponent) pagination!: PaginationComponent;

    navigationPath = "/admin/organizers"
    organizers: Organizer[] = [];
    functionToCall = this.adminService.getOrganizers

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private adminService: AdminService,
        private messageService: MessageService) {
    }

    onResult(event: Organizer[]) {
        this.organizers = event
    }

    revokeOrganizer(userId: number) {
        this.adminService.revokeOrganizer(userId).pipe(
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
