package nl.rso.axon.happergame.queryside.gameoverview.restcontrollers

import nl.rso.axon.happergame.coreapi.FetchGameOverviewQuery
import nl.rso.axon.happergame.coreapi.GameStatus
import nl.rso.axon.happergame.queryside.gameoverview.mongomodels.GameOverviewModel
import nl.rso.axon.happergame.queryside.gameoverview.mongomodels.GameOverviewModels
import nl.rso.axon.happergame.queryside.gameoverview.repositories.GameViewOverviewModelRepository
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux


@RestController
class GameOverviewRestController(private val queryGateway: QueryGateway, private val gameViewOverviewModelRepository: GameViewOverviewModelRepository) {

    @RequestMapping(value = ["/api/overview"], method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE))
    fun getGames(): Flux<GameOverviewModel> {
        val subscriptionQuery = queryGateway.subscriptionQuery(FetchGameOverviewQuery(null), GameOverviewModels::class.java,
                GameOverviewModel::class.java)

        return Flux.create { emitter ->
            subscriptionQuery.initialResult().subscribe({ gameOverViewModels -> gameOverViewModels.models.forEach({ emitter.next(it) }) })
            subscriptionQuery.updates().doOnComplete(Runnable { emitter.complete() }).subscribe({ emitter.next(it) })
        }
    }

    @RequestMapping(value = ["/api/walloffame"], method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getWallOfFameGames() : List<GameOverviewModel> {
        val gamesWithStatusWon = gameViewOverviewModelRepository.findAllByGameStats_GameStatusOrderByGameEndedDesc(GameStatus.WON)
        return gamesWithStatusWon.sortedBy { it.gameStats.movesHuman}.take(4)
    }
}