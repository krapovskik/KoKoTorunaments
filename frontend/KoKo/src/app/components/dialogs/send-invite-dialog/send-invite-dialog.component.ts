import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {MessageService} from "../../../service/message.service";
import {UserService} from "../../../service/user.service";

@Component({
    selector: 'app-send-invite-dialog',
    templateUrl: './send-invite-dialog.component.html',
    styleUrls: ['./send-invite-dialog.component.css']
})
export class SendInviteDialogComponent {
    inviteForm = this.formBuilder.group({
        email: ''
    });
    loading = false;

    constructor(
        private dialogRef: MatDialogRef<SendInviteDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public teamId: number,
        private formBuilder: FormBuilder,
        private userService: UserService,
        private messageService: MessageService) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    onSubmit() {

        if (this.inviteForm.invalid) {
            return;
        }

        this.loading = true;
        let email = this.inviteForm.controls['email'].value
        this.userService.sendInvite(email, this.teamId).subscribe({
            next: data => {
                this.messageService.showSuccessMessage(data.response)
                this.dialogRef.close()
            },
            error: err => {
                this.messageService.showErrorMessage(err.error.message)
                this.loading = false;
            }
        })
    }
}
