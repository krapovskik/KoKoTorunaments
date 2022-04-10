import {Component, OnInit, ViewChild} from '@angular/core';
import {CreateTeamDialogComponent} from "../header/create-team-dialog/create-team-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {TeamService} from "../../service/team.service";
import {MyTeams} from "../../model/MyTeams";
import {debounceTime, distinctUntilChanged, filter, mergeMap, of, switchMap} from "rxjs";
import {FormControl} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {TeamMember} from "../../model/TeamMember";
import {MessageService} from "../../service/message.service";
import {MatAccordion} from "@angular/material/expansion";

@Component({
    selector: 'app-my-teams',
    templateUrl: './my-teams.component.html',
    styleUrls: ['./my-teams.component.css']
})
export class MyTeamsComponent implements OnInit {

    @ViewChild(MatAccordion) accordion!: MatAccordion

    searchFormControl = new FormControl('');
    myTeams: MyTeams[] = []
    options: TeamMember[] = []

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

    sendInvite() {

    }

    openCreateTeamDialog() {
        const dialogRef = this.dialog.open(CreateTeamDialogComponent, {
            width: '500px',
        })

        dialogRef.afterClosed().pipe(
            mergeMap((result) => {
                if (result == 'success') {
                    return this.teamService.getMyTeams()
                }

                return of(this.myTeams)
            })
        ).subscribe({
            next: data => {
                this.searchFormControl.setValue('')
                this.myTeams = data
            }
        })
    }
}
