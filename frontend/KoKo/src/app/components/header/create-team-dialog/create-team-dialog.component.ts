import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {MessageService} from "../../../service/message.service";
import {TeamService} from "../../../service/team.service";
import {finalize} from "rxjs";

@Component({
    selector: 'app-create-team-dialog',
    templateUrl: './create-team-dialog.component.html',
    styleUrls: ['./create-team-dialog.component.css']
})
export class CreateTeamDialogComponent {
    createTeamForm = this.formBuilder.group({
        teamName: ''
    });
    loading = false;

    constructor(
        private dialogRef: MatDialogRef<CreateTeamDialogComponent>,
        private formBuilder: FormBuilder,
        private teamService: TeamService,
        private messageService: MessageService) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    onSubmit() {

        if (this.createTeamForm.invalid) {
            return;
        }

        this.loading = true;
        let teamName = this.createTeamForm.controls['teamName'].value

        this.teamService.createTeam(teamName)
            .pipe(finalize(() => {
                this.loading = false
            })).subscribe({
            next: (data) => {
                this.messageService.showSuccessMessage(data.response)
                this.dialogRef.close('success')
            },
            error: err => {
                this.messageService.showErrorMessage(err.error.message)
            }
        })
    }
}
