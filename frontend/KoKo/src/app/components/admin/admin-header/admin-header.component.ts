import {Component, EventEmitter, Output} from '@angular/core';
import {TokenService} from "../../../service/token.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-admin-header',
    templateUrl: './admin-header.component.html',
    styleUrls: ['./admin-header.component.css']
})
export class AdminHeaderComponent {

    @Output() toggleSideBarEvent: EventEmitter<any> = new EventEmitter();

    constructor(
        private tokenService: TokenService,
        private router: Router) {
    }

    logOut() {
        this.tokenService.logOut();
        this.router.navigate(["/"])
    }

    toggleSideBar() {
        this.toggleSideBarEvent.emit();
    }
}
