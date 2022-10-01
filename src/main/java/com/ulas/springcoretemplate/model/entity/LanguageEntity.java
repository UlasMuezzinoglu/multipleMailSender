package com.ulas.springcoretemplate.model.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ulas.springcoretemplate.model.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.ulas.springcoretemplate.constant.CollectionConstants.LANGUAGE;

@Getter
@Setter
@Document(value = LANGUAGE)
public class LanguageEntity extends BaseEntity {

    @JsonProperty("LANGUAGE")
    @Field
    private String language;

    @JsonProperty("ERROR")
    @Field
    private JSONObject error;

    @JsonProperty("SUCCESS")
    @Field
    private JSONObject success;

    @JsonProperty("TEXT")
    @Field
    private JSONObject texts;
}
