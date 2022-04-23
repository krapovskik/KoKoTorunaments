import {Opponent} from "./Opponent";

export interface Match {
    id: number,
    winner: number,
    isFinished: boolean,
    number: number,
    round: number,
    opponent1: Opponent,
    opponent2: Opponent,
}
