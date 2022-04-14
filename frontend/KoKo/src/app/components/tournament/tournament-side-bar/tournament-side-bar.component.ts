import {Component, EventEmitter, Output} from '@angular/core';

@Component({
    selector: 'app-tournament-side-bar',
    templateUrl: './tournament-side-bar.component.html',
    styleUrls: ['./tournament-side-bar.component.css']
})
export class TournamentSideBarComponent {

    @Output() toggleSideBarEvent: EventEmitter<any> = new EventEmitter();

    sideBarToggle() {
        this.toggleSideBarEvent.emit();
    }
}
