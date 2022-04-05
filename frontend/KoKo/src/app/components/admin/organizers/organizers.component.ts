import {Component, OnInit} from '@angular/core';
import {finalize, map, switchMap} from "rxjs";
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
    pageSize = 15;
    pageSizeOptions: number[] = [15, 50, 100];
    organizers: Organizer[] = [];

    constructor(private route: ActivatedRoute, private router: Router, private adminService: AdminService, private messageService: MessageService) {

    }

    ngOnInit(): void {
        this.$currentPage.pipe(
            switchMap(({pageIndex, pageSize}) => {
                    return this.adminService.getOrganizers(pageIndex, pageSize)
                }
            )).subscribe({
            next: (data) => {
                this.length = data.totalElements;
                this.organizers = data.content;
            }
        })
    }

    onPageEvent(pageEvent: PageEvent) {
        this.router.navigate([`/admin/organizers/${pageEvent.pageIndex + 1}/${pageEvent.pageSize}`])
    }

    revokeOrganizer(userId: number) {
        this.adminService.revokeOrganizer(userId).pipe(
            finalize(() => {
                this.organizers = this.organizers.filter(el => el.id != userId)
                if (this.organizers.length == 0 && this.pageIndex > 0) {
                    this.router.navigate([`/admin/organizers/${this.pageIndex}/${this.pageSize}`])
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
