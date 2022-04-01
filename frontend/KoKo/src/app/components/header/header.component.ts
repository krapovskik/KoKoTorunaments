import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {RegisterUserDialog} from "./registerUserDialog/register-user-dialog.component";
import {TokenService} from "../../service/token.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent {

    constructor(private dialog: MatDialog, private tokenService: TokenService, private router: Router) {
    }

    isLoggedIn(): boolean {
        return !!this.tokenService.getUser()
    }

    openDialog(): void {
        const dialogRef = this.dialog.open(RegisterUserDialog, {
            width: '500px',
        });
    }

    logOut() {
        this.tokenService.logOut();
        this.router.navigate(["/"])
    }
}
