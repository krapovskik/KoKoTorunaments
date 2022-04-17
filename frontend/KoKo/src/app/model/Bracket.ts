import {Tournament} from "./Tournament";
import {Match} from "./Match";

export interface Bracket {
    tournament: Tournament,
    matches: Match[],
}
