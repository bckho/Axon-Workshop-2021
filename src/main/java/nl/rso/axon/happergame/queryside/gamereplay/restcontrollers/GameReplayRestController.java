package nl.rso.axon.happergame.queryside.gamereplay.restcontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rso.axon.happergame.coreapi.ReplayModus;
import nl.rso.axon.happergame.coreapi.ReplayOrder;
import nl.rso.axon.happergame.coreapi.ReplayQuery;
import nl.rso.axon.happergame.queryside.gamereplay.mongomodels.GameReplayModel;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameReplayRestController {

    private final QueryGateway queryGateway;

    @RequestMapping(value="/api/games/replay/{gameid}/{replaymodus}",  method = {RequestMethod.GET}, produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public Flux<GameReplayModel> getGame(@PathVariable("gameid") final String gameId, @PathVariable("replaymodus") final ReplayModus replayModus) {

        final SubscriptionQueryResult<GameReplayModel, GameReplayModel> subscriptionQuery = queryGateway.subscriptionQuery(new ReplayQuery(gameId, replayModus, ReplayOrder.BACKWARD), GameReplayModel.class, GameReplayModel.class);

        subscriptionQuery.initialResult().subscribe();

        return subscriptionQuery.updates();
    }

    @RequestMapping(value="/api/games/replay/{gameid}",  method = {RequestMethod.GET}, produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public Flux<GameReplayModel> replayGame(@PathVariable("gameid") final String gameId) {

        final SubscriptionQueryResult<GameReplayModel, GameReplayModel> subscriptionQuery = queryGateway.subscriptionQuery(new ReplayQuery(gameId, ReplayModus.NORMAL , ReplayOrder.FORWARD), GameReplayModel.class, GameReplayModel.class);

        subscriptionQuery.initialResult().subscribe();

        return subscriptionQuery.updates();
    }

    @RequestMapping(value="/api/games/replaynewstyle/{gameid}/{replaymodus}/{replayorder}",  method = {RequestMethod.GET}, produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public Flux<GameReplayModel> replayGameNewStyle(@PathVariable("gameid") final String gameId, @PathVariable("replaymodus") final ReplayModus replayModus, @PathVariable("replayorder") final ReplayOrder replayOrder) {

        final SubscriptionQueryResult<GameReplayModel, GameReplayModel> subscriptionQuery = queryGateway.subscriptionQuery(new ReplayQuery(gameId, replayModus , replayOrder), GameReplayModel.class, GameReplayModel.class);

        subscriptionQuery.initialResult().subscribe();

        return subscriptionQuery.updates();
    }
}
