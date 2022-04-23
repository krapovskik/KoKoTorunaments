import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {MatDialog} from "@angular/material/dialog";
import {TournamentService} from "../../service/tournament.service";
import {FormControl} from "@angular/forms";
import {Tournament} from "../../model/Tournament";
import {ProfileTournament} from "../../model/ProfileTournament";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {Page} from "../../model/Page";
import {MessageService} from "../../service/message.service";

@Component({
    selector: 'app-my-profile',
    templateUrl: './my-profile.component.html',
    styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent implements OnInit {

    Highcharts = Highcharts
    chartOptions!: any;
    updateFlag = false;

    id!: number
    fullName = ''
    win = 0
    total = 0

    navigationPath!: string
    functionToCall!: (page: number, size: number) => Observable<Page<Tournament>>
    type = new FormControl('all')
    tournaments: ProfileTournament[] = []

    constructor(
        private dialog: MatDialog,
        private tournamentService: TournamentService,
        private route: ActivatedRoute,
        private messageService: MessageService
    ) {
    }

    ngOnInit(): void {

        this.id = +this.route.snapshot.paramMap.get('id')!
        this.functionToCall = this.tournamentService.getAllTournamentsByUser(this.id)
        this.navigationPath = `/profile/${this.id}`
        this.tournamentService.getProfileStatistics(this.id).subscribe({
            next: (data) => {
                this.fullName = data.response.fullName
                this.win = data.response.won
                this.total = data.response.total
                this.chartOptions.series[0].data = [{
                    name: 'Win',
                    y: (this.win / this.total * 100),
                }, {
                    name: 'Lose',
                    y: ((this.total - this.win) / this.total * 100)
                }]
                this.updateFlag = true
            },
            error: err => {
                this.messageService.showErrorMessage(err.error.message)
            }
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
    }

    onResult(event: ProfileTournament[]) {
        this.tournaments = event
    }

    changeImage() {
        console.log('change')
    }
}
