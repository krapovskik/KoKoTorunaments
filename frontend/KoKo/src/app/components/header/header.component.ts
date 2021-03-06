import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {RegisterUserDialog} from "../dialogs/registerUserDialog/register-user-dialog.component";
import {TokenService} from "../../service/token.service";
import {Router} from "@angular/router";
import {BecomeOrganizerDialogComponent} from "../dialogs/become-organizer-dialog/become-organizer-dialog.component";
import {CreateTournamentDialogComponent} from "../dialogs/create-tournament-dialog/create-tournament-dialog.component";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent {

    constructor(
        private dialog: MatDialog,
        public tokenService: TokenService,
        public router: Router) {
    }

    isLoggedIn(): boolean {
        return !!this.tokenService.getUser()
    }

    openRegisterDialog(): void {
        this.dialog.open(RegisterUserDialog, {
            width: '500px',
        });
    }

    openBecomeOrganizerDialog(): void {
        this.dialog.open(BecomeOrganizerDialogComponent, {
            width: '500px',
        });
    }

    openCreateTournament(): void {
        this.dialog.open(CreateTournamentDialogComponent, {
            width: '500px',
        });
    }

    logOut() {
        this.tokenService.logOut();
        this.router.navigate(["/"])
    }
}
