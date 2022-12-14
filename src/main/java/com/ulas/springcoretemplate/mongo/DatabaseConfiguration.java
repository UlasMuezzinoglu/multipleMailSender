package com.ulas.springcoretemplate.mongo;


import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.stereotype.Component;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
@RequiredArgsConstructor
public class DatabaseConfiguration {
    private final MongoClient mongoClient;
    private MongoDatabase database;
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    private CodecRegistry pojoCodecRegistry() {
        return fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider
                                .builder()
                                .automatic(true)
                                .build()
                )
        );
    }

    public Document fromBson(BsonDocument bson) {
        return pojoCodecRegistry()
                .get(Document.class)
                .decode(
                        bson.asBsonReader(),
                        DecoderContext.builder().build()
                );
    }

    @Bean
    public MongoDatabase database() {

        if (database != null)
            return database;

        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider
                                .builder()
                                .automatic(true)
                                .build()
                )
        );

        database = mongoClient
                .getDatabase(databaseName)
                .withCodecRegistry(pojoCodecRegistry);

        return database;
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
