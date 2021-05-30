package nl.rso.axon.happergame.queryside.gamereplay.queryhandlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rso.axon.happergame.coreapi.ReplayOrder;
import nl.rso.axon.happergame.coreapi.ReplayQuery;
import nl.rso.axon.happergame.queryside.gamereplay.mongomodels.GameReplayModel;
import nl.rso.axon.happergame.queryside.gamereplay.repositories.GameReplayModelRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplayQueryHandler {

    private final GameReplayModelRepository repository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    @QueryHandler
    public GameReplayModel replay(final ReplayQuery replayQuery) {

        List<GameReplayModel> replayModelsList;

        if(replayQuery.getOrder().equals(ReplayOrder.FORWARD)) {
            replayModelsList = repository.findAllByGameIdOrderByTimeStampAsc(replayQuery.getGameId());

        } else {
            replayModelsList = repository.findAllByGameIdOrderByTimeStampDesc(replayQuery.getGameId());
        }

        final Flux<GameReplayModel> replayModels       = Flux.fromIterable(replayModelsList);
        final Flux<GameReplayModel> tail               = replayModels.skip(1);
        final Flux<Long> deltas                        = replayModels.zipWith(tail, (l, r) -> Math.abs(r.getTimeStamp().toEpochMilli() - l.getTimeStamp().toEpochMilli()));
        final Flux<Long> deltasCompleet                = deltas.startWith(0L);
        final Flux<Long> deltasCompleetSum             = deltasCompleet.scan((x, y) -> x + y);

        final Flux<Long> replayTimer                   = deltasCompleetSum.flatMap(x -> Flux.just(x).delayElements(Duration.ofMillis((Double.valueOf(x / replayQuery.getReplayModus().getFactor()).longValue()))));
        final Flux<GameReplayModel> events             = replayModels.zipWith(replayTimer, (l, r) -> l);


        events.subscribe(model -> {
            queryUpdateEmitter.emit(ReplayQuery.class, query -> replayQuery.getGameId() == query.getGameId(), model);
        });

        return null;

    }
}