import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Tournament} from "../../../model/Tournament";
import {Participant} from "../../../model/Participant";
import {JoinTournamentDialogComponent} from "../../tournaments/joinToutnamentDialog/join-tournament-dialog.component";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {TokenService} from "../../../service/token.service";
import {MessageService} from "../../../service/message.service";
import {TournamentService} from "../../../service/tournament.service";
import {ViewPlayersInTeamDialogComponent} from "../view-players-in-team-dialog/view-players-in-team-dialog.component";
import {Router} from "@angular/router";

@Component({
    selector: 'app-tournament-side-bar',
    templateUrl: './tournament-side-bar.component.html',
    styleUrls: ['./tournament-side-bar.component.css']
})
export class TournamentSideBarComponent implements OnInit, OnDestroy {

    @Input() tournament!: Tournament;
    @Input() participants!: string[]

    @Output() toggleSideBarEvent: EventEmitter<any> = new EventEmitter();
    @Output() joinEvent: EventEmitter<any> = new EventEmitter();

    dialogRef!: MatDialogRef<ViewPlayersInTeamDialogComponent>

    constructor(private dialog: MatDialog,
                private tokenService: TokenService,
                private messageService: MessageService,
                private tournamentService: TournamentService,
                private router: Router
    ) {
    }

    ngOnDestroy(): void {
        if(this.dialogRef) {
            this.dialogRef.close()
        }
    }

    ngOnInit() {
    }

    sideBarToggle() {
        this.toggleSideBarEvent.emit();
    }

    isLoggedIn(): boolean {
        return !!this.tokenService.getUser()
    }

    joinTournament(type: String, tournamentId: number) {
        if (type == "TEAM") {
            let dialogRef = this.dialog.open(JoinTournamentDialogComponent, {
                width: '500px',
                data: tournamentId
            });

            dialogRef.afterClosed().subscribe({
                next: data => {
                    if (data == 'success') {
                        this.joinEvent.emit()
                    }
                }
            })
        } else {
            let appUserId = this.tokenService.getUser()?.id
            this.tournamentService.addPlayerToTournament(appUserId!, tournamentId)
                .subscribe({
                    next: data => {
                        this.messageService.showSuccessMessage(data.response)
                        this.joinEvent.emit()
                    },
                    error: data => this.messageService.showErrorMessage(data.error.message)
                })
        }
    }

    onClicked(participant: Participant) {
        this.dialogRef = this.dialog.open(ViewPlayersInTeamDialogComponent, {
            width: '500px',
            data: participant.participantId
        })
    }

}
