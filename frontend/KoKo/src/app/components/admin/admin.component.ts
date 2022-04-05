import {Component} from '@angular/core';

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.css']
})
export class AdminComponent {

    sideBarOpen = true;

    sideBarToggle() {
        this.sideBarOpen = !this.sideBarOpen
    }
}
