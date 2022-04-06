import {Component, OnInit} from '@angular/core';
import {finalize, Subject, switchMap} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {AdminService} from "../../../service/admin.service";
import {MessageService} from "../../../service/message.service";
import {PageEvent} from "@angular/material/paginator";
import {Organizer} from "../../../model/Organizer";

@Component({
    selector: 'app-organizers',
    templateUrl: './organizers.component.html',
    styleUrls: ['./organizers.component.css']
})
export class OrganizersComponent implements OnInit {

    $pageEvent = new Subject<{pageIndex: number, pageSize: number}>()

    loading = false;
    pageIndex = 0;
    length!: number;
    pageSize = 15;
    pageSizeOptions: number[] = [15, 50, 100];
    organizers: Organizer[] = [];

    constructor(private route: ActivatedRoute, private router: Router, private adminService: AdminService, private messageService: MessageService) {

    }

    ngOnInit(): void {
        this.$pageEvent.pipe(
            switchMap(({pageIndex, pageSize}) => {
                return this.adminService.getOrganizers(pageIndex, pageSize)
            })
        ).subscribe({
            next: data => {
                this.loading = false;
                this.length = data.totalElements;
                this.organizers = data.content;
                if(this.pageIndex >= data.totalPages) {
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
        this.router.navigate([`/admin/organizers/${this.pageIndex + 1}/${this.pageSize}`]);
    }

    onPageEvent(pageEvent: PageEvent) {
        this.pageIndex = pageEvent.pageIndex;
        this.pageSize = pageEvent.pageSize;
        this.newPageEvent();
    }

    revokeOrganizer(userId: number) {
        this.adminService.revokeOrganizer(userId).pipe(
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
