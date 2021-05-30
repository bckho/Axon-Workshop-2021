package nl.rso.axon.happergame.queryside.currentstate.mongomodels

import nl.rso.axon.happergame.queryside.model.GameViewModel
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "game_current_state_model")
data class GameCurrentStateModel(@Id val gameId: String,
                                     val gameViewModel: GameViewModel)