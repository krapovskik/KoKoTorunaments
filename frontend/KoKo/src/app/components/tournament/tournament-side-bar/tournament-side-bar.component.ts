import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Tournament} from "../../../model/Tournament";

@Component({
    selector: 'app-tournament-side-bar',
    templateUrl: './tournament-side-bar.component.html',
    styleUrls: ['./tournament-side-bar.component.css']
})
export class TournamentSideBarComponent {

    @Input() tournament!: Tournament;
    @Input() participants!: string[]

    @Output() toggleSideBarEvent: EventEmitter<any> = new EventEmitter();

    sideBarToggle() {
        this.toggleSideBarEvent.emit();
    }
}
