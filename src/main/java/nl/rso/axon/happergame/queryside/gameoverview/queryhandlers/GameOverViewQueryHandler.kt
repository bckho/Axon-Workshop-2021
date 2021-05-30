package nl.rso.axon.happergame.queryside.gameoverview.queryhandlers

import nl.rso.axon.happergame.coreapi.FetchGameOverviewQuery
import nl.rso.axon.happergame.queryside.gameoverview.mongomodels.GameOverviewModels
import nl.rso.axon.happergame.queryside.gameoverview.repositories.GameViewOverviewModelRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
open class GameOverViewQueryHandler(private val repository: GameViewOverviewModelRepository) {

    @QueryHandler
    open fun getGames(query: FetchGameOverviewQuery) : GameOverviewModels {
        return GameOverviewModels(repository.findAllByOrderByGameStartedDesc())
    }
}