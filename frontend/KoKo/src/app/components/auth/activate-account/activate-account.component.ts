import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../service/auth.service";
import {MessageService} from "../../../service/message.service";

@Component({
    selector: 'app-activate-account',
    templateUrl: './activate-account.component.html',
    styleUrls: ['./activate-account.component.css']
})
export class ActivateAccountComponent implements OnInit {

    token = '';
    activationForm!: FormGroup;
    loading = false;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authService: AuthService,
        private messageService: MessageService) {
    }

    ngOnInit(): void {
        this.activationForm = this.formBuilder.group({
            firstName: '',
            lastName: '',
            password: '',
        });

        if (this.route.snapshot.queryParams['token']) {
            this.token = this.route.snapshot.queryParams['token']
        }
    }

    onSubmit(): void {
        if (this.activationForm.invalid) {
            return;
        }
        this.loading = true

        let firstName = this.activationForm.controls['firstName'].value
        let lastName = this.activationForm.controls['lastName'].value
        let password = this.activationForm.controls['password'].value

        this.authService.activateAccount(this.token, firstName, lastName, password).subscribe({
            next: data => {
                this.messageService.showSuccessMessage(data.response)
                this.router.navigate(["/login"])
            },
            error: err => {
                this.messageService.showErrorMessage(err.error.message)
                this.loading = false;
            }
        })
    }
}
