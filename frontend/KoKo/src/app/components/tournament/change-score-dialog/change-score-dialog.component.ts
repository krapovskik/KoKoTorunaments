import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {filter} from "rxjs";
import {Tournament} from "../../../model/Tournament";
import {TournamentService} from "../../../service/tournament.service";
import {MessageService} from "../../../service/message.service";

@Component({
    selector: 'app-change-score-dialog',
    templateUrl: './change-score-dialog.component.html',
    styleUrls: ['./change-score-dialog.component.css']
})
export class ChangeScoreDialogComponent implements OnInit {

    isFinished = false
    loading = false;

    editForm = this.formBuilder.group({
        useNumberScore: false,
        score1: this.matchData.match.opponent1.score || 0,
        score2: this.matchData.match.opponent2.score || 0,
        isFinished: false,
        winner: null,
    })

    constructor(
        private dialogRef: MatDialogRef<ChangeScoreDialogComponent>,
        private formBuilder: FormBuilder,
        private tournamentService: TournamentService,
        private messageService: MessageService,
        @Inject(MAT_DIALOG_DATA) public matchData: {
            tournament: Tournament,
            match: { id: number, opponent1: { score: number }, opponent2: { score: number } }
            opponent1: { name: string },
            opponent2: { name: string }
        }
    ) {
    }

    ngOnInit(): void {

        this.setWinner()

        this.editForm.controls['useNumberScore'].valueChanges.subscribe({
            next: () => {
                this.setWinner()
            }
        })

        this.editForm.controls['score1'].valueChanges.pipe(
            filter((value) => !!value)
        ).subscribe({
            next: () => {
                this.setWinner()
            }
        })

        this.editForm.controls['score2'].valueChanges.subscribe({
            next: () => {
                this.setWinner()
            }
        })
    }

    setWinner() {
        let score1 = this.editForm.controls['score1'].value;
        let score2 = this.editForm.controls['score2'].value;
        let winner = this.editForm.controls['winner']

        if (score1 > score2) {
            winner.setValue(0)
        } else if (score1 < score2) {
            winner.setValue(1)
        } else {
            winner.setValue(null)
        }
    }

    onSubmit() {

        let useNumberScore = this.editForm.controls['useNumberScore'].value;
        let score1 = this.editForm.controls['score1'].value;
        let score2 = this.editForm.controls['score2'].value;
        let isFinished = this.editForm.controls['isFinished'].value;
        let winner = this.editForm.controls['winner'].value

        if ((useNumberScore && this.editForm.controls['score1'].invalid && this.editForm.controls['score2'].invalid)
            || (isFinished && winner == null)) {
            return
        }

        if (!useNumberScore) {
            score1 = null
            score2 = null
        }

        if (!isFinished) {
            winner = null
        }

        this.loading = true;
        this.tournamentService.editMatch(
            this.matchData.tournament.tournamentId,
            this.matchData.match.id,
            score1,
            score2,
            isFinished,
            winner
        ).subscribe({
            next: data => {
                this.messageService.showSuccessMessage(data.response)
                this.dialogRef.close('success')
            },
            error: err => {
                this.messageService.showErrorMessage(err.error.message)
                this.loading = false
            }
        })
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
