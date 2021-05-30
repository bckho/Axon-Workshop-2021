package nl.rso.axon.happergame.queryside.gamereplay.repositories

import nl.rso.axon.happergame.queryside.gamereplay.mongomodels.GameReplayModel
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface GameReplayModelRepository : MongoRepository<GameReplayModel, String> {
    fun findAllByGameIdOrderByTimeStampAsc(gameId: String): List<GameReplayModel>
    fun findAllByGameIdOrderByTimeStampDesc(gameId: String): List<GameReplayModel>
    fun findTopByGameIdOrderByInsertedDesc(gameId: String): Optional<GameReplayModel>
}