import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {RegisterUserDialog} from "../header/registerUserDialog/register-user-dialog.component";
import {TournamentService} from "../../service/tournament.service";
import {ChangeScoreDialogComponent} from "./change-score-dialog/change-score-dialog.component";

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


        // @ts-ignore
        window.bracketsViewer.onMatchClicked = (match) => {

            console.log(match)
            this.dialog.open(ChangeScoreDialogComponent, {
                width: '500px',
                data: match
            })
            // @ts-ignore
            // window.bracketsViewer.updateMatch(match);
        };
    }

    sideBarToggle() {
        this.sideBarOpen = !this.sideBarOpen
    }
}
