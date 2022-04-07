import {Component, OnInit, ViewChild} from '@angular/core';
import {AdminService} from "../../service/admin.service";
import {PaginationComponent} from "../pagination/pagination.component";

@Component({
    selector: 'app-all-tournaments',
    templateUrl: './all-tournaments.component.html',
    styleUrls: ['./all-tournaments.component.css']
})
export class AllTournamentsComponent implements OnInit {

    @ViewChild(PaginationComponent) pagination!: PaginationComponent;

    navigationPath = "/allTournaments/ongoing"
    functionToCall: any = this.adminService.getOrganizerRequests;

    title = 'ongoing'

    constructor(private adminService: AdminService) {
    }

    ngOnInit(): void {

    }

    onResult(event: any) {

    }
}
