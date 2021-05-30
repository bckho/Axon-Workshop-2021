package nl.rso.axon.happergame.queryside.gamereplay.eventprocessors

import nl.rso.axon.happergame.coreapi.*
import nl.rso.axon.happergame.queryside.gamereplay.mongomodels.GameReplayModel
import nl.rso.axon.happergame.queryside.gamereplay.repositories.GameReplayModelRepository
import nl.rso.axon.happergame.queryside.model.GameStats
import nl.rso.axon.happergame.queryside.model.GameViewModel
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class GameReplayProcessor(private val repository: GameReplayModelRepository) {

    @EventHandler
    fun handle(event: LevelGameCreatedEvent) {

        val gameViewModel = GameViewModel(startDate = event.creationDate,
                gameStats = GameStats(),
                simplePlayground = event.level.simplifiedPlayground.map({it.toString()}),
                width = event.level.width,
                height = event.level.height)

        repository.save(GameReplayModel(Instant.now().toEpochMilli(), event.gameId, gameViewModel, event.creationDate))
    }

    @EventHandler
    fun handle(event: FixedGameCreatedEvent) {

        val gameViewModel = GameViewModel(startDate = event.creationDate,
                gameStats = GameStats(),
                simplePlayground = event.simplePlayground,
                width = event.width,
                height = event.height)

        repository.save(GameReplayModel(Instant.now().toEpochMilli(), event.gameId, gameViewModel, event.creationDate))
    }


    @EventHandler
    fun handle(event: GameStatusChangedEvent) {

        val model = repository.findTopByGameIdOrderByInsertedDesc(event.gameId)

        if (model.isPresent) {
            repository.save(GameReplayModel(Instant.now().toEpochMilli(), event.gameId, model.get().gameViewModel.changeGameStatus(event.gameStatus), event.changeDate))
        }
    }

    @EventHandler
    fun handle(event: GameElementMovedEvent) {

        val model = repository.findTopByGameIdOrderByInsertedDesc(event.gameId)

        if (model.isPresent) {
            val toBeGameViewModel = model.get().gameViewModel.moveGameElement(event.from, event.to, event.gameElement)
            repository.save(GameReplayModel(Instant.now().toEpochMilli(), event.gameId, toBeGameViewModel, event.moveDate))
        }
    }

    @EventHandler
    fun handle(event: HumanBouncedEvent) {
        val model = repository.findTopByGameIdOrderByInsertedDesc(event.gameId)
        if (model.isPresent) {
            repository.save(GameReplayModel(Instant.now().toEpochMilli(), event.gameId, model.get().gameViewModel.humanBounced(), event.bounceDate))
        }
    }

}