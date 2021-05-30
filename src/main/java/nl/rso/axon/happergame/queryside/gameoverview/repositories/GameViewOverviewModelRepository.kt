package nl.rso.axon.happergame.queryside.gameoverview.repositories

import nl.rso.axon.happergame.coreapi.GameStatus
import nl.rso.axon.happergame.queryside.gameoverview.mongomodels.GameOverviewModel
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional


interface GameViewOverviewModelRepository : MongoRepository<GameOverviewModel, String> {
    fun findAllByOrderByGameStartedDesc(): List<GameOverviewModel>
    fun findByGameId(gameId: String): Optional<GameOverviewModel>
    fun findAllByGameStats_GameStatusOrderByGameEndedDesc(gameStatus: GameStatus): List<GameOverviewModel>
}