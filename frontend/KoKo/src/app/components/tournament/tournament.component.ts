import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {RegisterUserDialog} from "../header/registerUserDialog/register-user-dialog.component";
import {TournamentService} from "../../service/tournament.service";

@Component({
    selector: 'app-tournament',
    templateUrl: './tournament.component.html',
    styleUrls: ['./tournament.component.css']
})
export class TournamentComponent implements OnInit {

    sideBarOpen = false
    data = this.tournamentService.getTournamentBracket(0)

    constructor(
        private dialog: MatDialog,
        private tournamentService: TournamentService
    ) {
    }

    ngOnInit(): void {
        // @ts-ignore
        window.bracketsViewer.render(this.data)

        // @ts-ignore
        window.bracketsViewer.onMatchClicked = (match) => {

            console.log(match)
            this.dialog.open(RegisterUserDialog)
            // @ts-ignore
            // window.bracketsViewer.updateMatch(match);
        };
    }

    sideBarToggle() {
        this.sideBarOpen = !this.sideBarOpen
    }
}
