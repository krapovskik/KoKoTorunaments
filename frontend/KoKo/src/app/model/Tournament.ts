import {Participant} from "./Participant";

export interface Tournament {
    tournamentId: number
    tournamentName: string
    tournamentCategory: string
    tournamentActualParticipants: Participant[]
    tournamentNumberOfParticipants: number
    tournamentTimelineType: string
    tournamentType: string,
    organizerId: number,
    description: string,
    location: string,
    startingDate: string,
}
