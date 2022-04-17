export interface Match {
    id: number,
    winner: number,
    isFinished: boolean,
    number: number,
    round: number,
    opponent1: { id: number, name: string, score: number },
    opponent2: { id: number, name: string, score: number },
}
