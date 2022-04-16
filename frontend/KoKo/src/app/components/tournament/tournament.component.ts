import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {TournamentService} from "../../service/tournament.service";
import {ActivatedRoute} from "@angular/router";
import {mergeMap} from "rxjs";
import {Tournament} from "../../model/Tournament";
import {ChangeScoreDialogComponent} from "./change-score-dialog/change-score-dialog.component";

@Component({
    selector: 'app-tournament',
    templateUrl: './tournament.component.html',
    styleUrls: ['./tournament.component.css']
})
export class TournamentComponent implements OnInit {

    $tournamentId = this.route.paramMap.pipe(
        mergeMap((params) => {
            let id = +params.get("id")!
            return this.tournamentService.getTournamentBracket(id)
        })
    )

    tournament!: Tournament
    participants: string[] = []
    sideBarOpen = true

    constructor(
        private dialog: MatDialog,
        private tournamentService: TournamentService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit(): void {

        this.$tournamentId.subscribe({
            next: (data) => {
                console.log(data.response)
                this.tournament = data.response.tournament
                if (this.tournament.tournamentTimelineType != "COMING_SOON") {
                    let result = {
                        stages: [
                            {
                                id: 0,
                                tournament_id: this.tournament.tournamentId,
                                name: this.tournament.tournamentName,
                                type: 'single_elimination',
                                number: 1,
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
                                child_count: 0,
                                opponent1: match.opponent1?.id ? {
                                    id: match.opponent1.id,
                                    score: match.winner != null ? (match.winner == 0 ? 'W' : 'L') : '',
                                    result: match.winner != null ? (match.winner == 0 ? 'win' : 'loss') : '',
                                } : (match.round == 0 ? null : ''),
                                opponent2: match.opponent2?.id ? {
                                    id: match.opponent2.id,
                                    score: match.winner != null ? (match.winner == 1 ? 'W' : 'L') : '',
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
                } else {
                    window.bracketsViewer.render(this.tournamentService.getEmptyBracket(this.tournament.tournamentNumberOfParticipants / 2))
                }
            }
        })

        window.bracketsViewer.onMatchClicked = (match: any) => {
            if (!match.is_finished && match.opponent1 && match.opponent2) {
                this.dialog.open(ChangeScoreDialogComponent, {
                    width: '500px',
                    data: match
                })
            }
        };
    }

    sideBarToggle() {
        this.sideBarOpen = !this.sideBarOpen
    }
}
