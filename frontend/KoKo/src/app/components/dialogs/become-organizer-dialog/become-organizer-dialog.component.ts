import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {MessageService} from "../../../service/message.service";
import {UserService} from "../../../service/user.service";

@Component({
    selector: 'app-become-organizer-dialog',
    templateUrl: './become-organizer-dialog.component.html',
    styleUrls: ['./become-organizer-dialog.component.css']
})
export class BecomeOrganizerDialogComponent {

    becomeOrganizerForm = this.formBuilder.group({
        title: '',
        description: ''
    });
    loading = false;

    constructor(
        private dialogRef: MatDialogRef<BecomeOrganizerDialogComponent>,
        private formBuilder: FormBuilder,
        private userService: UserService,
        private messageService: MessageService) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    onSubmit() {

        if (this.becomeOrganizerForm.invalid) {
            return;
        }

        this.loading = true;
        let title = this.becomeOrganizerForm.controls['title'].value
        let description = this.becomeOrganizerForm.controls['description'].value

        this.userService.sendOrganizerRequest(title, description).subscribe({
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
