package com.ulas.springcoretemplate.mongo;


import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

@AllArgsConstructor
public class CustomAggregationOperation implements AggregationOperation {

    private Document document;

    @Override
    public Document toDocument(AggregationOperationContext aggregationOperationContext) {
        return aggregationOperationContext.getMappedObject(document);
    }
}
