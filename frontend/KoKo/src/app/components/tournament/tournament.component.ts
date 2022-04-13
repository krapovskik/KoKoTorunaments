import {Component} from '@angular/core';

@Component({
    selector: 'app-tournament',
    templateUrl: './tournament.component.html',
    styleUrls: ['./tournament.component.css']
})
export class TournamentComponent {

    sideBarOpen = false

    sideBarToggle() {
        this.sideBarOpen = !this.sideBarOpen
    }
}
