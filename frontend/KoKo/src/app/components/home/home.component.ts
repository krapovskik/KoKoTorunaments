import {Component, OnInit} from '@angular/core';
import {RegisterUserDialog} from "../header/registerUserDialog/register-user-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent {

    constructor(private dialog: MatDialog) {
    }

    openRegisterDialog(): void {
        this.dialog.open(RegisterUserDialog, {
            width: '500px',
        });
    }

}
