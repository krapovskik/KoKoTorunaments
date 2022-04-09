import {TeamMember} from "./TeamMember";

export interface MyTeams {
    teamId: number,
    teamName: string,
    teamMemberResponse: TeamMember[],
}
