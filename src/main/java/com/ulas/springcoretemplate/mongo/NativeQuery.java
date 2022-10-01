package com.ulas.springcoretemplate.mongo;

import org.bson.Document;

public final class NativeQuery {

    public static Document CONVERT_ID_TO_STRING() {
        return new Document("$addFields",
                new Document("id",
                        new Document("$toString", "$_id")
                )
        );
    }
}
