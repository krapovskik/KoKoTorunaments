import {Component, OnInit} from '@angular/core';
import {TournamentService} from "../../service/tournament.service";
import {ActivatedRoute} from "@angular/router";
import {map, Observable} from "rxjs";
import {Page} from "../../model/Page";
import {Tournament} from "../../model/Tournament";

@Component({
    selector: 'app-all-tournaments',
    templateUrl: './all-tournaments.component.html',
    styleUrls: ['./all-tournaments.component.css']
})
export class AllTournamentsComponent implements OnInit {

    $type = this.route.paramMap.pipe(
        map(param => param.get('type'))
    );

    navigationPath!: string
    functionToCall!: (pageIndex: number, pageSize: number) => Observable<Page<any>>;
    titles = ['ongoing', 'finished', 'comingsoon']
    currentIndex = 0
    nextIndex = 1;
    prevIndex = 2;

    tournaments: Tournament[] = [];

    constructor(private tournamentService: TournamentService, private route: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.$type.subscribe({
            next: data => {
                switch (data) {
                    case 'ongoing':
                        this.functionToCall = this.tournamentService.getOngoingTournaments
                        this.currentIndex = 0;
                        break;
                    case 'finished':
                        this.functionToCall = this.tournamentService.getFinishedTournaments
                        this.currentIndex = 1;
                        break;
                    case 'comingsoon':
                        this.functionToCall = this.tournamentService.getComingSoonTournaments
                        this.currentIndex = 2;
                        break;
                }

                this.navigationPath = `/allTournaments/${data}`
                this.nextIndex = (this.currentIndex + 1) % this.titles.length;
                this.prevIndex = (this.currentIndex - 1) == -1 ? 2 : this.currentIndex - 1;
            }
        })
    }

    onResult(event: Tournament[]) {
        this.tournaments = event;
    }

    joinTournament(){}
}
