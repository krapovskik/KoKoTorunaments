import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../../service/auth.service";
import {MessageService} from "../../../service/message.service";
import {TokenService} from "../../../service/token.service";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    loginForm!: FormGroup;
    loading = false;

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private authService: AuthService,
        private messageService: MessageService,
        private tokenService: TokenService) {
    }

    ngOnInit(): void {
        this.loginForm = this.formBuilder.group({
            username: '',
            password: '',
        });
    }

    onSubmit(): void {
        if (this.loginForm.invalid) {
            return;
        }
        this.loading = true

        let username = this.loginForm.controls['username'].value;
        let password = this.loginForm.controls['password'].value;

        this.authService.login(username, password).subscribe({
            next: user => {
                this.tokenService.saveUser(user)
                this.tokenService.saveToken(user.token)
                if (user.role == 'ADMIN') {
                    this.router.navigate(['/admin/organizerRequests/1/15'])
                } else {
                    this.router.navigate(['/tournaments'])
                }
            },
            error: () => {
                this.messageService.showErrorMessage("Incorrect username or password")
                this.loading = false;
            }
        })
    }
}
