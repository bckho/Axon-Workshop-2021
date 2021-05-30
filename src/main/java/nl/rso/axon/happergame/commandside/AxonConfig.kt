package nl.rso.axon.happergame.commandside

import com.mongodb.MongoClient
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.eventhandling.scheduling.java.SimpleEventScheduler
import org.axonframework.extensions.mongo.DefaultMongoTemplate
import org.axonframework.extensions.mongo.eventhandling.saga.repository.MongoSagaStore
import org.axonframework.modelling.saga.repository.SagaStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors


@Configuration
@ComponentScan
open class AxonConfig {

    @Bean
    @Autowired
    open fun eventScheduler(eventBus: EventBus, transactionManager: TransactionManager): EventScheduler {
        return SimpleEventScheduler
                .builder()
                .eventBus(eventBus)
                .scheduledExecutorService(Executors.newScheduledThreadPool(4))
                .transactionManager(transactionManager).build()
    }

    @Bean
    @Autowired
    open fun mySagaStore(@Qualifier ("SagaMongoTemplate") defaultMongoTemplate: DefaultMongoTemplate) : SagaStore<Any> {
        return MongoSagaStore.builder()
                .mongoTemplate(defaultMongoTemplate).build();
    }

    @Bean("SagaMongoTemplate")
    @Autowired
    open fun defaultMongoTemplate(mongoClient: MongoClient) : DefaultMongoTemplate  {
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongoClient, "happer_game_axon_sagas")
                .sagasCollectionName("SAGAENTRY")
                .build()
    }

}