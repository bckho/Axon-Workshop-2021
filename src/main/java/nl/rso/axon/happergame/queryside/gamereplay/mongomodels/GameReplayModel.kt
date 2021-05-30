package nl.rso.axon.happergame.queryside.gamereplay.mongomodels


import nl.rso.axon.happergame.queryside.model.GameViewModel
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "game_replay_model")
data class  GameReplayModel(
        val inserted: Long,
        val gameId: String,
        val gameViewModel: GameViewModel,
        val timeStamp: Instant)