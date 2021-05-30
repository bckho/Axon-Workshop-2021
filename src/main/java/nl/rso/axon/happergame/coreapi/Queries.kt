package nl.rso.axon.happergame.coreapi

import nl.rso.axon.happergame.queryside.model.GameViewModel


data class FetchGameOverviewQuery(val gameStatus: GameStatus?)

data class ReplayQuery(val gameId: String, val replayModus: ReplayModus, val order: ReplayOrder)
data class UpdateGameQuery(val gameId: String)
data class UpdateGameResponse(val gameViewModel: GameViewModel)
enum class ReplayModus(val factor: Double) {
    SLOW(0.5),
    NORMAL(1.0),
    FAST(2.0)
}
enum class ReplayOrder {
    FORWARD, BACKWARD
}