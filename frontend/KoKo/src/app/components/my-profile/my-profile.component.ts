import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {MatDialog} from "@angular/material/dialog";
import {TournamentService} from "../../service/tournament.service";
import {FormControl} from "@angular/forms";
import {Tournament} from "../../model/Tournament";
import {ProfileTournament} from "../../model/ProfileTournament";
import {ActivatedRoute, Router} from "@angular/router";
import {debounceTime, distinctUntilChanged, filter, map, mergeMap, Observable, switchMap} from "rxjs";
import {Page} from "../../model/Page";
import {MessageService} from "../../service/message.service";
import {ChangeProfileIconDialogComponent} from "./change-profile-icon-dialog/change-profile-icon-dialog.component";
import {TokenService} from "../../service/token.service";
import {UserService} from "../../service/user.service";
import {Player} from 'src/app/model/Player';
import {ProfileStatistics} from "../../model/ProfileStatistics";
import {Response} from "../../model/Response";

@Component({
    selector: 'app-my-profile',
    templateUrl: './my-profile.component.html',
    styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent implements OnInit {

    searchFormControl = new FormControl('');
    options: Player[] = []

    Highcharts = Highcharts
    chartOptions!: any;
    updateFlag = false;

    id!: number
    fullName = ''
    win = 0
    loss = 0
    others = 0
    total = 0
    profilePhoto!: string

    navigationPath!: string
    functionToCall!: (page: number, size: number) => Observable<Page<Tournament>>
    type = new FormControl('all')
    tournaments: ProfileTournament[] = []

    constructor(
        private dialog: MatDialog,
        private tournamentService: TournamentService,
        private route: ActivatedRoute,
        private messageService: MessageService,
        private tokenService: TokenService,
        private userService: UserService,
        private router: Router
    ) {
    }

    ngOnInit(): void {

        this.route.paramMap.pipe(
            map(params => +params.get('id')!),
            mergeMap(data => {
                this.id = data
                this.functionToCall = this.tournamentService.getAllTournamentsByUser(this.id)
                this.navigationPath = `/profile/${this.id}`
                return this.tournamentService.getProfileStatistics(data)
            })
        ).subscribe({
            next: data => this.setData(data),
            error: err => this.messageService.showErrorMessage(err.error.message)
        })


        this.type.valueChanges.subscribe({
            next: (data) => {
                if (data == 'all') {
                    this.functionToCall = this.tournamentService.getAllTournamentsByUser(this.id)
                } else {
                    this.functionToCall = this.tournamentService.getWonTournamentsByUser(this.id)
                }
            }
        })

        this.chartOptions = {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            tooltip: {
                pointFormat: '<b>{point.percentage:.1f}%</b>'
            },
            accessibility: {
                point: {
                    valueSuffix: '%'
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                    }
                }
            },
            series: [{
                colorByPoint: true,
                data: []
            }]
        }

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

    onResult(event: ProfileTournament[]) {
        this.tournaments = event
    }

    changeImage() {
        let userId = this.tokenService.getUser()?.id
        if (userId == this.id) {
            let dialogRef = this.dialog.open(
                ChangeProfileIconDialogComponent,
                {
                    width: '500px',
                    data: this.profilePhoto
                }
            )
            dialogRef.afterClosed().subscribe({
                next: data => {
                    if (data == "success") {
                        this.getProfileStatistics()
                    }
                }
            })
        }

    }

    getProfileStatistics() {
        this.tournamentService.getProfileStatistics(this.id).subscribe({
            next: (data) => this.setData(data),
            error: err => {
                this.messageService.showErrorMessage(err.error.message)
            }
        })
    }

    setData(data: Response<ProfileStatistics>) {
        this.fullName = data.response.fullName
        this.win = data.response.won
        this.loss = data.response.loss
        this.others = data.response.others
        this.total = this.win + this.loss + this.others
        this.profilePhoto = data.response.profilePhoto
        this.chartOptions.series[0].data = [{
            name: 'Win',
            y: (this.win / this.total * 100),
        }, {
            name: 'Lose',
            y: ((this.loss) / this.total * 100)
        }, {
            name: 'Others',
            y: ((this.others) / this.total * 100)
        }]
        this.updateFlag = true
    }

    onEnter(event: any) {
        if (event.key == "Enter") {
            let userId = +this.searchFormControl.value?.split('-')[1];
            if (userId) {
                this.router.navigate(["profile", userId, 1, 15])
                this.type.setValue('all')
            }
        }
    }
}
