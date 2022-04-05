import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {AuthService} from "../../../service/auth.service";
import {MessageService} from "../../../service/message.service";

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
        private authService: AuthService,
        private messageService: MessageService
    ) {
    }

    onNoClick(): void {
        this.dialogRef.close();
        this.dialogRef.componentInstance
    }

    onSubmit() {

        if (this.becomeOrganizerForm.invalid) {
            return;
        }

        this.loading = true;
        let title = this.becomeOrganizerForm.controls['title'].value
        let description = this.becomeOrganizerForm.controls['description'].value

        console.log(title)
        console.log(description)
    }
}
