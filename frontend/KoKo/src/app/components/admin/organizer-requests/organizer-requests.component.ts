import {Component, OnInit} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {ActivatedRoute, Router} from "@angular/router";
import {finalize, map, switchMap} from "rxjs";
import {AdminService} from "../../../service/admin.service";
import {OrganizerRequests} from "../../../model/OrganizerRequests";
import {MessageService} from "../../../service/message.service";

@Component({
    selector: 'app-organizer-requests',
    templateUrl: './organizer-requests.component.html',
    styleUrls: ['./organizer-requests.component.css']
})
export class OrganizerRequestsComponent implements OnInit {

    $currentPage = this.route.paramMap.pipe(
        map((params) => {
            let currentPage = params.get('currentPage')
            let size = params.get('size')
            this.pageIndex = +currentPage! - 1;
            this.pageSize = +size!;
            return {pageIndex: this.pageIndex, pageSize: this.pageSize};
        })
    )

    pageIndex = 0;
    length!: number;
    pageSize = 1;
    pageSizeOptions: number[] = [1, 50, 100];
    organizerRequests: OrganizerRequests[] = [];

    constructor(private route: ActivatedRoute, private router: Router, private adminService: AdminService, private messageService: MessageService) {
    }

    ngOnInit(): void {
        this.$currentPage.pipe(
            switchMap(({pageIndex, pageSize}) => {
                    return this.adminService.getOrganizerRequests(pageIndex, pageSize)
                }
            )).subscribe({
            next: (data) => {
                this.length = data.totalElements;
                this.organizerRequests = data.content;
            }
        })
    }

    onPageEvent(pageEvent: PageEvent) {
        this.router.navigate([`/admin/organizerRequests/${pageEvent.pageIndex + 1}/${pageEvent.pageSize}`])
    }

    approveRequest(requestId: number) {
        this.adminService.approveRequest(requestId).pipe(
            finalize(() => {
                this.organizerRequests = this.organizerRequests.filter(el => el.requestId != requestId)
                if (this.organizerRequests.length == 0 && this.pageIndex > 0) {
                    this.router.navigate([`/admin/organizerRequests/${this.pageIndex}/${this.pageSize}`])
                }
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
                this.organizerRequests = this.organizerRequests.filter(el => el.requestId != requestId)
                if (this.organizerRequests.length == 0 && this.pageIndex > 0) {
                    this.router.navigate([`/admin/organizerRequests/${this.pageIndex}/${this.pageSize}`])
                }
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
