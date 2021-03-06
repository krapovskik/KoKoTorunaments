import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormControl} from "@angular/forms";
import {MessageService} from "../../../service/message.service";
import {map} from "rxjs";
import {TeamService} from "../../../service/team.service";
import {Team} from "../../../model/Team";
import {TournamentService} from "../../../service/tournament.service";

@Component({
    selector: 'app-join-tournament-dialog',
    templateUrl: './join-tournament-dialog.component.html',
    styleUrls: ['./join-tournament-dialog.component.css']
})
export class JoinTournamentDialogComponent implements OnInit {

    teamFormControl = new FormControl("");
    loading = false;
    teams: Team[] = []
    filteredTeams: Team[] = []

    constructor(
        private dialogRef: MatDialogRef<JoinTournamentDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public tournamentId: number,
        private messageService: MessageService,
        private teamService: TeamService,
        private tournamentService: TournamentService
    ) {
    }

    ngOnInit(): void {
        this.teamService.getTeamsForUser().subscribe({
            next: data => this.teams = data
        })

        this.teamFormControl.valueChanges.pipe(
            map(text => (text ? this._filter(text) : this.teams.slice())),
        ).subscribe({
            next: data => {
                this.filteredTeams = data
            }
        })
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    onSubmit() {
        this.loading = true;
        let searchedTeam = this.teamFormControl.value

        let team = this.teams.filter(team => team.teamName == searchedTeam)

        if (team.length == 0) {
            this.messageService.showErrorMessage("Please select given teams.")
            this.loading = false
        } else {
            this.tournamentService.addTeamToTournament(team[0].teamId, this.tournamentId)
                .subscribe({
                    next: data => {
                        this.messageService.showSuccessMessage(data.response)
                        this.dialogRef.close("success");
                    },
                    error: err => {
                        this.messageService.showErrorMessage(err.error.message)
                        this.loading = false
                    }
                })
        }


    }

    private _filter(text: string): Team[] {
        const filterValue = text.toLowerCase();
        return this.teams.filter(team => team.teamName.toLowerCase().includes(filterValue));
    }
}
