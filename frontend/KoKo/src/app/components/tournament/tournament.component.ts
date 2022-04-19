import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {TournamentService} from "../../service/tournament.service";
import {ActivatedRoute, Router} from "@angular/router";
import {mergeMap} from "rxjs";
import {Tournament} from "../../model/Tournament";
import {ChangeScoreDialogComponent} from "./change-score-dialog/change-score-dialog.component";
import {TokenService} from "../../service/token.service";
import fx from "fireworks";

@Component({
    selector: 'app-tournament',
    templateUrl: './tournament.component.html',
    styleUrls: ['./tournament.component.css']
})
export class TournamentComponent implements OnInit, OnDestroy {

    $tournamentId = this.route.paramMap.pipe(
        mergeMap((params) => {
            let id = +params.get("id")!
            return this.tournamentService.getTournamentBracket(id)
        })
    )

    tournament!: Tournament
    participants: string[] = []
    sideBarOpen = true

    routeReuse: any

    constructor(
        private dialog: MatDialog,
        private tournamentService: TournamentService,
        private route: ActivatedRoute,
        private router: Router,
        private tokenService: TokenService,
    ) {
        router.onSameUrlNavigation = 'reload'
        this.routeReuse = router.routeReuseStrategy.shouldReuseRoute
        router.routeReuseStrategy.shouldReuseRoute = () => false
    }

    ngOnDestroy(): void {
        this.router.onSameUrlNavigation = 'ignore'
        this.router.routeReuseStrategy.shouldReuseRoute = this.routeReuse
    }

    ngOnInit(): void {

        this.$tournamentId.subscribe({
            next: (data) => {
                console.log(data.response)
                this.tournament = data.response.tournament
                this.tournament.description = this.tournament.description.replace(/\n/g, '<br/>')
                if (this.tournament.tournamentTimelineType != "COMING_SOON") {

                    if (this.tournament.tournamentTimelineType == 'FINISHED') {
                        this.startFireworks()
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
                    this.participants = [...new Set(result.participants.map(participant => participant.name))]
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
                                        this.router.navigate(['tournament', this.tournament.tournamentId])
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

    }

    startFireworks() {
        for (let i = 0; i < 50; i++) {
            setTimeout(() => {
                fx({
                    x: Math.random() * (window.innerWidth - 150 - 150) + 150,
                    y: Math.random() * (window.innerHeight - 150 - 150) + 150,
                    colors: ['#673ab7', '#ffd740', '#f44336']
                })
            }, 50 * i)
        }
    }

    sideBarToggle() {
        this.sideBarOpen = !this.sideBarOpen
    }
}
