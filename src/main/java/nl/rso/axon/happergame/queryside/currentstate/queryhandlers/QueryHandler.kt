package nl.rso.axon.happergame.queryside.currentstate.queryhandlers

import nl.rso.axon.happergame.coreapi.UpdateGameQuery
import nl.rso.axon.happergame.queryside.currentstate.repositories.GameCurrentStateModelRepository

import nl.rso.axon.happergame.queryside.model.GameViewModel
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
open class QueryHandler(private val repositoryGame: GameCurrentStateModelRepository) {

    @QueryHandler
    fun on(query: UpdateGameQuery) : GameViewModel? {
        return repositoryGame.findByGameId(query.gameId)?.gameViewModel
    }
}