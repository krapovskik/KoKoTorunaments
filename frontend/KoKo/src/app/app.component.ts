import {Component, OnInit} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {MessageService} from "./service/message.service";
import {TokenService} from "./service/token.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

    constructor(
        private snackBar: MatSnackBar,
        private messageService: MessageService,
        public tokenService: TokenService) {
    }

    ngOnInit(): void {
        this.messageService.$successMessages.subscribe({
            next: message => {
                this.openMessage(message, 'success')
            }
        });

        this.messageService.$errorMessages.subscribe({
            next: message => {
                this.openMessage(message, 'error')
            }
        });
    }

    openMessage(message: string, type: string) {
        this.snackBar.open(message, 'X', {
            horizontalPosition: "center",
            verticalPosition: "bottom",
            duration: 3000,
            panelClass: [`${type}Alert`]
        });
    }
}
