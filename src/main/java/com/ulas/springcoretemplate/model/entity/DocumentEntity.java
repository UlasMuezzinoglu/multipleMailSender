package com.ulas.springcoretemplate.model.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ulas.springcoretemplate.model.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "document")
@CompoundIndexes({
        @CompoundIndex(def = "{'name':1, 'language':1}", name = "name_language")
})
public class DocumentEntity extends BaseEntity {
    @JsonProperty("DOC")
    @Field
    private String doc;

    @JsonProperty("NAME")
    @Field
    private String name;

    @JsonProperty("LANGUAGE")
    @Field
    private String language;
}
