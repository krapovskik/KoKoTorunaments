import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import fx from "fireworks";

@Component({
    selector: 'app-winner-dialog',
    templateUrl: './winner-dialog.component.html',
    styleUrls: ['./winner-dialog.component.css']
})
export class WinnerDialogComponent implements OnInit, OnDestroy {

    constructor(
        private dialogRef: MatDialogRef<WinnerDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public winner: string
    ) {
    }

    ngOnInit() {
        this.startFireworks()
    }

    ngOnDestroy() {

    }

    close() {
        this.dialogRef.close()
    }

    startFireworks() {
        for (let i = 0; i < 20; i++) {
            setTimeout(() => {
                fx({
                    x: Math.random() * (window.innerWidth - 150 - 150) + 150,
                    y: Math.random() * (window.innerHeight - 150 - 150) + 150,
                    colors: ['#673ab7', '#ffd740', '#f44336']
                })
            }, 50 * i)
        }
    }
}
