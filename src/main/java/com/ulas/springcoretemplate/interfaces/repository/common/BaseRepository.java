package com.ulas.springcoretemplate.interfaces.repository.common;


import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.ulas.springcoretemplate.constant.ErrorConstants;
import com.ulas.springcoretemplate.exception.CustomException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.ArrayList;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends MongoRepository<T, ID> {

    default T findById(String id, MongoDatabase mongoDatabase, String collection, Class<T> targetClass) {
        MongoCursor<T> mongoCursor = mongoDatabase.getCollection(collection, targetClass)
                .aggregate(List.of(
                        new Document("$match", new Document("_id", new ObjectId(id))),
                        new Document("$addFields",
                                new Document("_id",
                                        new Document("$toString", "$_id")
                                )
                        )
                )).cursor();
        if (!mongoCursor.hasNext()) throw new CustomException(ErrorConstants.NOT_FOUND);
        return mongoCursor.next();
    }

    default T findByUserId(String id, MongoDatabase mongoDatabase, String collection, Class<T> targetClass) {
        MongoCursor<T> mongoCursor = mongoDatabase.getCollection(collection, targetClass)
                .aggregate(List.of(
                        new Document("$match", new Document("userId", id)),
                        new Document("$addFields",
                                new Document("_id",
                                        new Document("$toString", "$_id")
                                )
                        )
                )).cursor();
        return mongoCursor.next();
    }

    default T findByIdAndUserId(String id, String userId, MongoDatabase mongoDatabase, String collection, Class<T> targetClass) {
        MongoCursor<T> mongoCursor = mongoDatabase.getCollection(collection, targetClass)
                .aggregate(List.of(
                        new Document("$match", new Document("userId", userId)
                                .append("_id", new ObjectId(id))),
                        new Document("$addFields",
                                new Document("_id",
                                        new Document("$toString", "$_id")
                                )
                        )
                )).cursor();
        if (!mongoCursor.hasNext()) throw new CustomException(ErrorConstants.NOT_FOUND);
        return mongoCursor.next();
    }

    default List<T> findAll(MongoDatabase mongoDatabase, String collection, Class<T> targetClass) {
        List<T> items = new ArrayList<>();
        MongoCursor<T> mongoCursor = mongoDatabase.getCollection(collection, targetClass)
                .aggregate(List.of(
                        new Document("$addFields",
                                new Document("_id",
                                        new Document("$toString", "$_id")
                                )
                        )
                )).cursor();
        while (mongoCursor.hasNext()) items.add(mongoCursor.next());
        return items;
    }
}
