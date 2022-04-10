import { Component } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {AuthService} from "../../../service/auth.service";
import {MessageService} from "../../../service/message.service";

@Component({
  selector: 'app-join-tournament-dialog',
  templateUrl: './join-tournament-dialog.component.html',
  styleUrls: ['./join-tournament-dialog.component.css']
})
export class JoinTournamentDialogComponent {

    registerForm = this.formBuilder.group({
        email: ''
    });
    loading = false;

    constructor(
        private dialogRef: MatDialogRef<JoinTournamentDialogComponent>,
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private messageService: MessageService) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    onSubmit() {

        if (this.registerForm.invalid) {
            return;
        }

        this.loading = true;
        let email = this.registerForm.controls['email'].value

        this.authService.register(email).subscribe({
            next: data => {
                this.messageService.showSuccessMessage(data.response)
                this.onNoClick()
            },
            error: err => {
                this.messageService.showErrorMessage(err.error.message)
                this.loading = false
            }
        })
    }

}
