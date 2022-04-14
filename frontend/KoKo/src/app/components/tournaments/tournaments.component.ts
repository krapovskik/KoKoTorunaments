import {Component, OnInit} from '@angular/core';
import {TournamentService} from "../../service/tournament.service";
import {Tournament} from "../../model/Tournament";
import {MessageService} from "../../service/message.service";
import {MatDialog} from "@angular/material/dialog";
import {JoinTournamentDialogComponent} from "./joinToutnamentDialog/join-tournament-dialog.component";
import {TokenService} from "../../service/token.service";

@Component({
    selector: 'app-tournaments',
    templateUrl: './tournaments.component.html',
    styleUrls: ['./tournaments.component.css']
})
export class TournamentsComponent implements OnInit {

    ongoingTournaments: Tournament[] = []
    finishedTournaments: Tournament[] = []
    comingSoonTournaments: Tournament[] = []

    constructor(private tournamentService: TournamentService,
                private messageService: MessageService,
                private dialog: MatDialog,
                private tokenService: TokenService
    ) {
    }

    ngOnInit(): void {
        this.tournamentService.findAllTournaments().subscribe({
            next: data => {
                this.ongoingTournaments = data['ONGOING']
                this.finishedTournaments = data['FINISHED']
                this.comingSoonTournaments = data['COMING_SOON']
            },
            error: data => {
                this.messageService.showErrorMessage(data.error.message)
            }
        })
    }

    isLoggedIn(): boolean {
        return !!this.tokenService.getUser()
    }

    joinTournament(type: String, tournamentId: number) {
        if (type == "TEAM")
            this.dialog.open(JoinTournamentDialogComponent, {
                width: '500px',
                data: tournamentId
            });
    }

}
