package com.ulas.springcoretemplate.service.home;


import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.ulas.springcoretemplate.interfaces.service.home.DocumentService;
import com.ulas.springcoretemplate.model.entity.DocumentEntity;
import com.ulas.springcoretemplate.model.request.DocRequest;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final MongoDatabase mongoDatabase;

    @Override
    public <Res> Res getDoc(DocRequest docRequest) {
        return getDocAsLanguage(docRequest) != null ?
                (Res) getDocAsLanguage(docRequest) : (Res) getDocAsTurkish(docRequest);
    }

    private DocumentEntity getDocAsTurkish(DocRequest docRequest) {
        MongoCursor<DocumentEntity> docAsTurkish = mongoDatabase.getCollection("document", DocumentEntity.class).aggregate(Arrays.asList(
                new Document("$addFields", new Document("_id", new Document("$toString", "$_id"))),
                new Document("$match", new Document("name", docRequest.getName().getName()).append(
                        "language", "tr-TR"
                ))
        )).cursor();
        return docAsTurkish.hasNext() ? docAsTurkish.next() : null;
    }

    private DocumentEntity getDocAsLanguage(DocRequest docRequest) {
        MongoCursor<DocumentEntity> docAsEnglish = mongoDatabase.getCollection("document", DocumentEntity.class).aggregate(Arrays.asList(
                new Document("$addFields", new Document("_id", new Document("$toString", "$_id"))),
                new Document("$match", new Document("name", docRequest.getName().getName()).append(
                        "language", docRequest.getLanguage()
                ))
        )).cursor();
        return docAsEnglish.hasNext() ? docAsEnglish.next() : null;
    }
}

