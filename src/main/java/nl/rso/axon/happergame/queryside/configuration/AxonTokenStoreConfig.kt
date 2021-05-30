package nl.rso.axon.happergame.queryside.configuration

import com.mongodb.MongoClient
import org.axonframework.extensions.mongo.DefaultMongoTemplate
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore
import org.axonframework.serialization.Serializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class AxonTokenStoreConfig {

    @Bean("AxonTrackingProcessor")
    @Autowired
    open fun defaultMongoTemplate(mongoClient: MongoClient) : DefaultMongoTemplate {
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongoClient, "happer_game_axon_tracking_processors")
                .trackingTokensCollectionName("TOKENENTRY")
                .build()
    }

    @Bean
    @Autowired
    open fun myTokenStore(@Qualifier("AxonTrackingProcessor") defaultMongoTemplate: DefaultMongoTemplate, serializer: Serializer): MongoTokenStore {
        return MongoTokenStore.builder()
                .mongoTemplate(defaultMongoTemplate)
                .serializer(serializer)
                .build();
    }

}