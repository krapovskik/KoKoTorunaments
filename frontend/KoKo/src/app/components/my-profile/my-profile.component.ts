import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {BecomeOrganizerDialogComponent} from "../header/become-organizer-dialog/become-organizer-dialog.component";

@Component({
    selector: 'app-my-profile',
    templateUrl: './my-profile.component.html',
    styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent implements OnInit {

    image = 0

    constructor(private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    changeImg() {
        this.image++
    }

    openDialog() {
        this.dialog.open(BecomeOrganizerDialogComponent)
    }
}
