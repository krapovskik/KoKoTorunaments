import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
    selector: 'app-change-score-dialog',
    templateUrl: './change-score-dialog.component.html',
    styleUrls: ['./change-score-dialog.component.css']
})
export class ChangeScoreDialogComponent implements OnInit {

    constructor(@Inject(MAT_DIALOG_DATA) public match: any) {
        console.log(match)
    }

    ngOnInit(): void {
    }

}
