import {Component, OnInit} from '@angular/core';
import {RegisterUserDialog} from "../dialogs/registerUserDialog/register-user-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {TokenService} from "../../service/token.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    constructor(
        private dialog: MatDialog,
        private tokenService: TokenService,
        private router: Router) {
    }

    openRegisterDialog(): void {
        this.dialog.open(RegisterUserDialog, {
            width: '500px',
        });
    }

    ngOnInit(): void {
        let user = this.tokenService.getUser()
        if (user) {
            this.router.navigate(['/tournaments'])
        }
    }
}
