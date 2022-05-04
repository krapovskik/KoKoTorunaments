import {Component, Inject, OnInit} from '@angular/core';
import {Participant} from "../../../model/Participant";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {TeamService} from "../../../service/team.service";
import {map} from "rxjs";

@Component({
    selector: 'app-view-players-in-team-dialog',
    templateUrl: './view-players-in-team-dialog.component.html',
    styleUrls: ['./view-players-in-team-dialog.component.css']
})
export class ViewPlayersInTeamDialogComponent implements OnInit {

    participants: Participant[] = []

    constructor(
        private dialogRef: MatDialogRef<ViewPlayersInTeamDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public participantId: number,
        private teamService: TeamService
    ) {
    }

    ngOnInit(): void {
        this.teamService.findAllPlayersByTeam(this.participantId).pipe(
            map(data => data.response.map(player => {
                        let splited = player.fullNameId.split('-')
                        let participantName = splited[0]
                        let participantId = +splited[1]
                        return {
                            participantId,
                            participantName
                        } as Participant
                    }
                )
            )
        ).subscribe({
            next: data => this.participants = data
        })
    }

    onClose() {
        this.dialogRef.close()
    }

}
