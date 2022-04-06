import {Component, OnInit} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {ActivatedRoute, Router} from "@angular/router";
import {finalize, Subject, switchMap} from "rxjs";
import {AdminService} from "../../../service/admin.service";
import {OrganizerRequests} from "../../../model/OrganizerRequests";
import {MessageService} from "../../../service/message.service";

@Component({
    selector: 'app-organizer-requests',
    templateUrl: './organizer-requests.component.html',
    styleUrls: ['./organizer-requests.component.css']
})
export class OrganizerRequestsComponent implements OnInit {

    $pageEvent = new Subject<{ pageIndex: number, pageSize: number }>()

    loading = false;
    pageIndex = 0;
    length!: number;
    pageSize = 15;
    pageSizeOptions: number[] = [15, 50, 100];
    organizerRequests: OrganizerRequests[] = [];

    constructor(private route: ActivatedRoute, private router: Router, private adminService: AdminService, private messageService: MessageService) {
    }

    ngOnInit(): void {

        this.$pageEvent.pipe(
            switchMap(({pageIndex, pageSize}) => {
                return this.adminService.getOrganizerRequests(pageIndex, pageSize)
            })
        ).subscribe({
            next: data => {
                this.loading = false;
                this.length = data.totalElements;
                this.organizerRequests = data.content;
                if (this.pageIndex >= data.totalPages) {
                    this.pageIndex = data.totalPages - 1;
                    this.newPageEvent();
                }
            }
        })

        this.pageIndex = +this.route.snapshot.params['currentPage'] - 1;
        this.pageSize = +this.route.snapshot.params['size'];
        this.newPageEvent();
    }

    newPageEvent() {
        this.loading = true;
        this.$pageEvent.next({pageIndex: this.pageIndex, pageSize: this.pageSize})
        this.router.navigate([`/admin/organizerRequests/${this.pageIndex + 1}/${this.pageSize}`]);
    }

    onPageEvent(pageEvent: PageEvent) {
        this.pageIndex = pageEvent.pageIndex;
        this.pageSize = pageEvent.pageSize;
        this.newPageEvent();
    }

    approveRequest(requestId: number) {
        this.adminService.approveRequest(requestId).pipe(
            finalize(() => {
                this.newPageEvent();
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
                this.newPageEvent();
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
