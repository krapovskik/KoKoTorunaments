import {Component} from "@angular/core";
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {AuthService} from "../../../service/auth.service";
import {MessageService} from "../../../service/message.service";

@Component({
    selector: 'register-user-dialog',
    templateUrl: './register-user-dialog.component.html',
    styleUrls: ['./register-user-dialog.component.css']
})
export class RegisterUserDialog {

    registerForm = this.formBuilder.group({
        email: ''
    });
    loading = false;

    constructor(
        private dialogRef: MatDialogRef<RegisterUserDialog>,
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
