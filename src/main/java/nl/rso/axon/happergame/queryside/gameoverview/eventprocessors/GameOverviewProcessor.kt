package nl.rso.axon.happergame.queryside.gameoverview.eventprocessors


import lombok.extern.slf4j.Slf4j
import nl.rso.axon.happergame.coreapi.*
import nl.rso.axon.happergame.queryside.gameoverview.mongomodels.GameOverviewModel
import nl.rso.axon.happergame.queryside.gameoverview.repositories.GameViewOverviewModelRepository
import nl.rso.axon.happergame.queryside.model.GameStats
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.stereotype.Component

@Component
@Slf4j
class GameOverviewProcessor(private val repository: GameViewOverviewModelRepository, private val emitter: QueryUpdateEmitter) {
    @EventHandler
    fun on(event: GameCreatedEvent) {
        val gameOverviewModel = GameOverviewModel(event.gameId, GameStats(gameStatus = GameStatus.GAME_ON), event.creationDate, null)

        emitter.emit(FetchGameOverviewQuery::class.java, { query: FetchGameOverviewQuery -> true}, gameOverviewModel )
        repository.save(gameOverviewModel)
    }


    @EventHandler
    fun handle(event: GameElementMovedEvent) {
        val model = repository.findByGameId(event.gameId)
        if (model.isPresent) {
            val toBeStats = model.get().gameStats.moveGameElement(event.gameElement)
            val toBeGameViewModel = model.get().copy(gameStats = toBeStats)
            emitter.emit(FetchGameOverviewQuery::class.java, { query: FetchGameOverviewQuery -> true}, toBeGameViewModel )
            repository.save(toBeGameViewModel)
        }
    }

    @EventHandler
    fun handle(event: HumanBouncedEvent) {
        val model = repository.findByGameId(event.gameId)
        if (model.isPresent) {
            val toBeStats = model.get().gameStats.humanBounced()
            val toBeGameViewModel = model.get().copy(gameStats = toBeStats)
            emitter.emit(FetchGameOverviewQuery::class.java, { query: FetchGameOverviewQuery -> true}, toBeGameViewModel )
            repository.save(toBeGameViewModel)
        }
}

    @EventHandler
    fun on(event: GameStatusChangedEvent) {

        val model = repository.findByGameId(event.gameId)
        model?.let {
            val retrievedModel = model.get()
            val toBeGameOverviewModel = retrievedModel.copy(gameStats = retrievedModel.gameStats.changeGameStatus(event.gameStatus))
            emitter.emit(FetchGameOverviewQuery::class.java, { query: FetchGameOverviewQuery -> true}, toBeGameOverviewModel )
            repository.save(toBeGameOverviewModel)
        }
    }

    @EventHandler
    fun on(event: GameEndedEvent) {
        val model = repository.findByGameId(event.gameId)
        model?.let {
            val toBeGameOverviewModel = model.get().copy(gameStats = model.get().gameStats.changeGameStatus(event.gameStatus), gameEnded = event.endDate)
            emitter.emit(FetchGameOverviewQuery::class.java, { query: FetchGameOverviewQuery -> true}, toBeGameOverviewModel )
            repository.save(toBeGameOverviewModel)
        }

    }
}