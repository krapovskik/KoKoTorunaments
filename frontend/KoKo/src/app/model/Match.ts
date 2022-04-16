export interface Match {
    id: number,
    winner: number,
    isFinished: boolean,
    number: number,
    round: number,
    opponent1: { id: number, name: string },
    opponent2: { id: number, name: string },
}
