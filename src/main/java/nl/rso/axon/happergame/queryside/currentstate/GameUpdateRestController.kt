package nl.rso.axon.happergame.queryside.currentstate

import nl.rso.axon.happergame.coreapi.UpdateGameQuery
import nl.rso.axon.happergame.queryside.model.GameViewModel
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux


@RestController
class GameUpdateRestController(private val queryGateway: QueryGateway) {


    @RequestMapping("/api/game/update/{gameId}", produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE))
    fun receiveUpdates(@PathVariable gameId: String) : Flux<GameViewModel> {

        val gameViewModelFlux = this.queryGateway
                .subscriptionQuery(UpdateGameQuery(gameId), GameViewModel::class.java, GameViewModel::class.java)
                .updates()

        return gameViewModelFlux
    }
}