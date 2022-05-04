import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {TournamentService} from "../../../service/tournament.service";
import {MessageService} from "../../../service/message.service";

@Component({
    selector: 'app-create-tournament-dialog',
    templateUrl: './create-tournament-dialog.component.html',
    styleUrls: ['./create-tournament-dialog.component.css']
})
export class CreateTournamentDialogComponent implements OnInit {

    createTournamentForm = this.formBuilder.group({
        tournamentName: '',
        tournamentCategory: '',
        tournamentLocation: '',
        tournamentDescription: '',
        numberOfParticipants: '',
        tournamentType: '',
        tournamentDate: '',
        tournamentTime: ''
    });

    numberOfParticipants: Array<number> = []
    tournamentTypes: Array<string> = []
    minDate: Date | undefined

    loading = false;


    constructor(
        private dialogRef: MatDialogRef<CreateTournamentDialogComponent>,
        private formBuilder: FormBuilder,
        private tournamentService: TournamentService,
        private messageService: MessageService
    ) {
    }

    ngOnInit(): void {
        this.generateNumberOfParticipants()
        this.tournamentService.getTournamentTypes()
            .subscribe({
                next: data => this.tournamentTypes = data
            })
        this.minDate = new Date()
        this.minDate.setDate(this.minDate.getDate() + 1)
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    onSubmit() {

        if (this.createTournamentForm.invalid) {
            return;
        }

        this.loading = true;

        let tournamentName = this.createTournamentForm.controls['tournamentName'].value
        let tournamentCategory = this.createTournamentForm.controls['tournamentCategory'].value
        let tournamentLocation = this.createTournamentForm.controls['tournamentLocation'].value
        let tournamentDescription = this.createTournamentForm.controls['tournamentDescription'].value
        let numberOfParticipants = this.createTournamentForm.controls['numberOfParticipants'].value
        let tournamentType = this.createTournamentForm.controls['tournamentType'].value
        let original = this.createTournamentForm.controls['tournamentDate'].value
        let tournamentTime = this.createTournamentForm.controls['tournamentTime'].value

        let d = new Date(original)
        let tournamentDate = new Date(d.getFullYear(), d.getMonth(), d.getDate(),
            d.getHours(), d.getMinutes() - d.getTimezoneOffset()).toISOString();

        this.tournamentService.createTournament(tournamentName,
            tournamentCategory,
            tournamentLocation,
            tournamentDescription,
            numberOfParticipants,
            tournamentType,
            tournamentDate,
            tournamentTime)
            .subscribe({
                next: data => {
                    this.messageService.showSuccessMessage(data.response)
                    this.dialogRef.close()
                },
                error: err => {
                    this.loading = false
                    this.messageService.showErrorMessage(err.error.message)
                }
            })
    }

    generateNumberOfParticipants() {
        let start = 4;
        for (let i = 0; i < 5; i++) {
            this.numberOfParticipants.push(start)
            start *= 2
        }
    }

}
