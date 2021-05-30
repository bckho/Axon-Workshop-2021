package nl.rso.axon.happergame.queryside.currentstate.eventprocessors

import lombok.extern.slf4j.Slf4j
import nl.rso.axon.happergame.coreapi.*
import nl.rso.axon.happergame.queryside.currentstate.mongomodels.GameCurrentStateModel
import nl.rso.axon.happergame.queryside.currentstate.repositories.GameCurrentStateModelRepository
import nl.rso.axon.happergame.queryside.model.GameStats
import nl.rso.axon.happergame.queryside.model.GameViewModel
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.stereotype.Component

@Component
@Slf4j
class GameCurrentStateProcessor(private val repositoryGame: GameCurrentStateModelRepository, private val queryUpdateEmitter: QueryUpdateEmitter) {


    @EventHandler
    fun on(event: LevelGameCreatedEvent) {

        val simpleGameViewModel = GameViewModel(startDate = event.creationDate,
                simplePlayground = event.level.simplifiedPlayground.map({it.toString()}),
                gameStats = GameStats(),
                width = event.level.width,
                height = event.level.height)

        queryUpdateEmitter.emit(UpdateGameQuery::class.java, { query: UpdateGameQuery -> event.gameId == query.gameId}, UpdateGameResponse(simpleGameViewModel))
        repositoryGame.save(GameCurrentStateModel(event.gameId, simpleGameViewModel))
    }

    @EventHandler
    fun on(event: FixedGameCreatedEvent) {
        val simpleGameViewModel = GameViewModel(startDate = event.creationDate,
                simplePlayground = event.simplePlayground,
                gameStats = GameStats(),
                width = event.width,
                height = event.height)

        queryUpdateEmitter.emit(UpdateGameQuery::class.java, { query: UpdateGameQuery -> event.gameId == query.gameId}, UpdateGameResponse(simpleGameViewModel))
        repositoryGame.save(GameCurrentStateModel(event.gameId, simpleGameViewModel))
    }

    @EventHandler
    fun on(event: GameStatusChangedEvent) {

        val model = repositoryGame.findByGameId(event.gameId)

        model?.let{
            val retrievedModel = model!!
            val toBeModel = retrievedModel.copy(gameViewModel = retrievedModel.gameViewModel.changeGameStatus(event.gameStatus))
            queryUpdateEmitter.emit(UpdateGameQuery::class.java, { query: UpdateGameQuery -> retrievedModel.gameId == query.gameId }, UpdateGameResponse(toBeModel.gameViewModel))
            repositoryGame.save(toBeModel)

        }
    }

    @EventHandler
    fun on(event: GameEndedEvent) {
        val model = repositoryGame.findByGameId(event.gameId)
        model?.let{
            val retrievedModel = model!!
            val toBeModel = retrievedModel.gameViewModel.endGame(event.endDate).changeGameStatus(event.gameStatus)
            queryUpdateEmitter.emit(UpdateGameQuery::class.java, { query: UpdateGameQuery -> retrievedModel.gameId == query.gameId }, UpdateGameResponse(toBeModel))

            repositoryGame.save(retrievedModel.copy(gameViewModel = toBeModel.changeGameStatus(event.gameStatus)))
        }
    }

    @EventHandler
    fun on(event: GameElementMovedEvent) {
        val model = repositoryGame.findByGameId(event.gameId)
        model?.let{

            val retrievedModel = model!!
            val toBeGameViewModel = retrievedModel.gameViewModel.moveGameElement(event.from, event.to, event.gameElement)
            val toBeModel = retrievedModel.copy(gameViewModel = toBeGameViewModel)

            queryUpdateEmitter.emit(UpdateGameQuery::class.java, { query: UpdateGameQuery -> retrievedModel.gameId == query.gameId}, UpdateGameResponse(toBeModel.gameViewModel))
            repositoryGame.save(toBeModel)
        }
    }

    @EventHandler
    fun handle(event: HumanBouncedEvent) {

        val model = repositoryGame.findByGameId(event.gameId)

        model?.let{
            val retrievedModel = model!!
            val toBeModel = retrievedModel.copy(gameViewModel = retrievedModel.gameViewModel.humanBounced())

            queryUpdateEmitter.emit(UpdateGameQuery::class.java, { query: UpdateGameQuery -> retrievedModel.gameId == query.gameId}, UpdateGameResponse(toBeModel.gameViewModel))
            repositoryGame.save(toBeModel)
        }
    }
}


