import {Component, OnInit, ViewChild} from '@angular/core';
import {CreateTeamDialogComponent} from "../dialogs/create-team-dialog/create-team-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {TeamService} from "../../service/team.service";
import {MyTeams} from "../../model/MyTeams";
import {debounceTime, distinctUntilChanged, filter, mergeMap, switchMap} from "rxjs";
import {FormControl} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {Player} from "../../model/Player";
import {MessageService} from "../../service/message.service";
import {MatAccordion} from "@angular/material/expansion";
import {SendInviteDialogComponent} from "../dialogs/send-invite-dialog/send-invite-dialog.component";

@Component({
    selector: 'app-my-teams',
    templateUrl: './my-teams.component.html',
    styleUrls: ['./my-teams.component.css']
})
export class MyTeamsComponent implements OnInit {

    @ViewChild(MatAccordion) accordion!: MatAccordion

    searchFormControl = new FormControl('');
    myTeams: MyTeams[] = []
    options: Player[] = []

    constructor(
        private dialog: MatDialog,
        private teamService: TeamService,
        private userService: UserService,
        private messageService: MessageService) {
    }

    ngOnInit(): void {
        this.teamService.getMyTeams().subscribe({
            next: data => {
                this.myTeams = data
            }
        })

        this.searchFormControl.valueChanges.pipe(
            filter(value => !!value),
            debounceTime(400),
            distinctUntilChanged(),
            switchMap((value) => this.userService.searchUser(value))
        ).subscribe({
            next: data => {
                this.options = data
            }
        })
    }

    addUserToTeam(teamId: number) {
        if (this.searchFormControl.value !== '') {
            let userId = +this.searchFormControl.value?.split('-')[1];

            this.teamService.addUserToTeam(userId, teamId).pipe(
                mergeMap((data) => {
                    this.messageService.showSuccessMessage(data.response)
                    this.searchFormControl.setValue('')
                    return this.teamService.findAllPlayersByTeam(teamId)
                })
            ).subscribe({
                next: data => {
                    this.myTeams
                        .filter(team => team.teamId == teamId)[0]
                        .teamMemberResponse = data.response
                },
                error: err => {
                    this.messageService.showErrorMessage(err.error.message)
                }
            })
        }
    }

    sendInvite(teamId: number) {
        this.dialog.open(SendInviteDialogComponent, {
            width: '500px',
            data: teamId,
        })
    }

    openCreateTeamDialog() {
        const dialogRef = this.dialog.open(CreateTeamDialogComponent, {
            width: '500px',
        })

        dialogRef.afterClosed().pipe(
            filter((result) => result == 'success'),
            mergeMap(() => {
                return this.teamService.getMyTeams()
            })
        ).subscribe({
            next: data => {
                this.searchFormControl.setValue('')
                this.myTeams = data
            }
        })
    }
}
