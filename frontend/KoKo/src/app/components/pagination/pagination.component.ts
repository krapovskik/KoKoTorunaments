import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {finalize, Observable, Subject, switchMap} from "rxjs";
import {Organizer} from "../../model/Organizer";
import {ActivatedRoute, Router} from "@angular/router";
import {AdminService} from "../../service/admin.service";
import {MessageService} from "../../service/message.service";
import {PageEvent} from "@angular/material/paginator";
import {HttpClient} from "@angular/common/http";

@Component({
    selector: 'app-pagination',
    templateUrl: './pagination.component.html',
    styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit {

    $pageEvent = new Subject<{ pageIndex: number, pageSize: number }>()

    @Input() navigationPath!: string;
    @Input() functionToCall!: (pageIndex: number, pageSize: number, http: HttpClient) => Observable<any>;

    @Output() result = new EventEmitter();

    loading = false;
    pageIndex = 0;
    length!: number;
    pageSize = 15;
    pageSizeOptions: number[] = [15, 50, 100];
    organizers: Organizer[] = [];

    constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router, private adminService: AdminService, private messageService: MessageService) {

    }

    ngOnInit(): void {
        this.$pageEvent.pipe(
            switchMap(({pageIndex, pageSize}) => {
                return this.functionToCall(pageIndex, pageSize, this.http);
            })
        ).subscribe({
            next: data => {
                this.loading = false;
                this.length = data.totalElements;
                this.organizers = data.content;
                if (this.pageIndex >= data.totalPages) {
                    this.pageIndex = data.totalPages - 1;
                    this.newPageEvent();
                }

                this.result.emit(this.organizers);
            }
        })

        this.pageIndex = +this.route.snapshot.params['currentPage'] - 1;
        this.pageSize = +this.route.snapshot.params['size'];
        this.newPageEvent();
    }

    newPageEvent() {
        this.loading = true;
        this.$pageEvent.next({pageIndex: this.pageIndex, pageSize: this.pageSize})
        this.router.navigate([`${this.navigationPath}/${this.pageIndex + 1}/${this.pageSize}`]);
    }

    onPageEvent(pageEvent: PageEvent) {
        this.pageIndex = pageEvent.pageIndex;
        this.pageSize = pageEvent.pageSize;
        this.newPageEvent();
    }

    revokeOrganizer(userId: number) {
        this.loading = true;
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
