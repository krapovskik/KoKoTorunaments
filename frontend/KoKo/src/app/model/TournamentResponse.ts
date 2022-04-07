import {Tournament} from "./Tournament";

export interface TournamentResponse {
    COMING_SOON: Tournament[],
    ONGOING: Tournament[],
    FINISHED: Tournament[]
}
