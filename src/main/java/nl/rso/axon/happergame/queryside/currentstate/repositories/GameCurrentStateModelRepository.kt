package nl.rso.axon.happergame.queryside.currentstate.repositories

import nl.rso.axon.happergame.queryside.currentstate.mongomodels.GameCurrentStateModel
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface GameCurrentStateModelRepository : MongoRepository<GameCurrentStateModel, String> {
    fun findByGameId(gameId: String) : GameCurrentStateModel?
}