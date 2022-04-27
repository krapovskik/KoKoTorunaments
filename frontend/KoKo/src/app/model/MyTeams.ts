import {Player} from "./Player";

export interface MyTeams {
    teamId: number,
    teamName: string,
    teamMemberResponse: Player[],
}
