import {Component, ElementRef, OnDestroy, OnInit, Renderer2, ViewChild} from '@angular/core';
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {TournamentService} from "../../service/tournament.service";
import {ActivatedRoute} from "@angular/router";
import {mergeMap, Subject} from "rxjs";
import {Tournament} from "../../model/Tournament";
import {ChangeScoreDialogComponent} from "./change-score-dialog/change-score-dialog.component";
import {TokenService} from "../../service/token.service";
import {WinnerDialogComponent} from "./winner-dialog/winner-dialog.component";

@Component({
    selector: 'app-tournament',
    templateUrl: './tournament.component.html',
    styleUrls: ['./tournament.component.css']
})
export class TournamentComponent implements OnInit, OnDestroy {

    $tournamentBracket = new Subject()
    @ViewChild("brackets") bracketDiv!: ElementRef
    winnerDialog!: MatDialogRef<WinnerDialogComponent>;

    tournament!: Tournament
    sideBarOpen = true

    constructor(
        private dialog: MatDialog,
        private tournamentService: TournamentService,
        private route: ActivatedRoute,
        private tokenService: TokenService,
        private renderer: Renderer2
    ) {
    }

    ngOnInit(): void {
        this.$tournamentBracket.pipe(
            mergeMap(() => this.route.paramMap),
            mergeMap((params) => {
                let id = +params.get("id")!
                return this.tournamentService.getTournamentBracket(id)
            })
        ).subscribe({
            next: (data) => {
                console.log(data.response)
                for (let child of this.bracketDiv.nativeElement.childNodes) {
                    this.renderer.setProperty(child, 'innerHTML', '')
                }
                this.tournament = data.response.tournament
                this.tournament.description = this.tournament.description.replace(/\n/g, '<br/>')
                if (this.tournament.tournamentTimelineType != "COMING_SOON") {

                    if (this.tournament.tournamentTimelineType == 'FINISHED') {
                        let finalMatch = data.response.matches.reduce((m1, m2) => {
                            return (m1.round > m2.round) ? m1 : m2
                        })

                        let winner = finalMatch.winner == 0 ? finalMatch.opponent1 : finalMatch.opponent2

                        this.winnerDialog = this.dialog.open(WinnerDialogComponent, {
                            hasBackdrop: false,
                            data: winner.name,
                        })
                    }

                    let result = {
                        stages: [
                            {
                                id: 0,
                                tournament_id: this.tournament.tournamentId,
                                name: this.tournament.tournamentName,
                                type: 'single_elimination',
                                settings: {},
                            },
                        ],
                        matches: data.response.matches.map((match) => {
                            return {
                                id: match.id,
                                stage_id: 0,
                                group_id: 0,
                                round_id: match.round,
                                is_finished: match.isFinished,
                                opponent1: match.opponent1?.id ? {
                                    id: match.opponent1.id,
                                    score: match.opponent1.score != null ? match.opponent1.score
                                        : (match.winner != null ? (match.winner == 0 ? 'Won' : 'Loss') : ''),
                                    result: match.winner != null ? (match.winner == 0 ? 'win' : 'loss') : '',
                                } : (match.round == 0 ? null : ''),
                                opponent2: match.opponent2?.id ? {
                                    id: match.opponent2.id,
                                    score: match.opponent2.score != null ? match.opponent2.score
                                        : (match.winner != null ? (match.winner == 1 ? 'Won' : 'Loss') : ''),
                                    result: match.winner != null ? (match.winner == 1 ? 'win' : 'loss') : '',
                                } : (match.round == 0 ? null : ''),
                            }
                        }),
                        participants: data.response.matches.flatMap((match) => match.opponent1)
                            .concat(data.response.matches.flatMap((match) => match.opponent2))
                            .filter((team) => team)
                            .map((team) => {
                                return {
                                    id: team.id,
                                    tournament_id: this.tournament.tournamentId,
                                    name: team.name
                                }
                            })
                    }
                    window.bracketsViewer.render(result)

                    window.bracketsViewer.onMatchClicked = (match: any) => {
                        let id = this.tokenService.getUser()?.id
                        if (id == this.tournament.organizerId && !match.is_finished && match.opponent1 && match.opponent2) {
                            let dialogRef = this.dialog.open(ChangeScoreDialogComponent, {
                                width: '500px',
                                data: {
                                    tournament: this.tournament,
                                    match: match,
                                    opponent1: result.participants.filter(el => el.id == match.opponent1.id)[0],
                                    opponent2: result.participants.filter(el => el.id == match.opponent2.id)[0]
                                }
                            })

                            dialogRef.afterClosed().subscribe({
                                next: data => {
                                    if (data == 'success') {
                                        this.$tournamentBracket.next('')
                                    }
                                }
                            })
                        }
                    };
                } else {
                    window.bracketsViewer.render(this.tournamentService
                        .getEmptyBracket(this.tournament.tournamentNumberOfParticipants / 2))
                }
            }
        })
        this.$tournamentBracket.next('')
    }

    sideBarToggle() {
        this.sideBarOpen = !this.sideBarOpen
    }

    onJoin() {
        // if(this.tournament.tournamentTimelineType != "COMING_SOON")
        this.$tournamentBracket.next('')
    }

    ngOnDestroy() {
        if(this.winnerDialog) {
            this.winnerDialog.close()
        }
    }
}
