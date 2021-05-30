package nl.rso.axon.happergame.queryside.gameoverview.mongomodels

import nl.rso.axon.happergame.queryside.model.GameStats
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "game_over_view_model")
data class GameOverviewModel(@Id val gameId: String,
                             val gameStats: GameStats,
                             val gameStarted: Instant,
                             val gameEnded: Instant?)


data class GameOverviewModels(val models: List<GameOverviewModel>)