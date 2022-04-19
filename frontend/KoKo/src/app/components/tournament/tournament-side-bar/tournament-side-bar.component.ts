import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Tournament} from "../../../model/Tournament";
import {JoinTournamentDialogComponent} from "../../tournaments/joinToutnamentDialog/join-tournament-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {TokenService} from "../../../service/token.service";
import {MessageService} from "../../../service/message.service";
import {TournamentService} from "../../../service/tournament.service";

@Component({
    selector: 'app-tournament-side-bar',
    templateUrl: './tournament-side-bar.component.html',
    styleUrls: ['./tournament-side-bar.component.css']
})
export class TournamentSideBarComponent {

    @Input() tournament!: Tournament;
    @Input() participants!: string[]

    @Output() toggleSideBarEvent: EventEmitter<any> = new EventEmitter();

    constructor(private dialog: MatDialog,
                private tokenService: TokenService,
                private messageService: MessageService,
                private tournamentService: TournamentService) {
    }

    sideBarToggle() {
        this.toggleSideBarEvent.emit();
    }

    isLoggedIn(): boolean {
        return !!this.tokenService.getUser()
    }

    joinTournament(type: String, tournamentId: number) {
        if (type == "TEAM") {
            this.dialog.open(JoinTournamentDialogComponent, {
                width: '500px',
                data: tournamentId
            });
        } else {
            let appUserId = this.tokenService.getUser()?.id
            this.tournamentService.addPlayerToTournament(appUserId!, tournamentId)
                .subscribe({
                    next: data => this.messageService.showSuccessMessage(data.response),
                    error: data => this.messageService.showErrorMessage(data.error.message)
                })
        }
    }
}
