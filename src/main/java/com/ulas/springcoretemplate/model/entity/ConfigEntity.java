package com.ulas.springcoretemplate.model.entity;


import com.ulas.springcoretemplate.model.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(value = "config")
public class ConfigEntity extends BaseEntity {

    @Field
    private Integer imageQuality;

    @Field
    private Integer imageReSizeWidth;

    @Field
    private Integer imageReSizeHeight;

    @Field
    private Boolean isForceUpdate;

    @Field
    private Boolean isForce;

    @Field
    private Boolean isForceToHint;

    @Field
    private Boolean isMaintenance;

    @Field
    private Boolean optionalUpdate;

    @Field
    private String dynamicText;

    @Field
    private Boolean isDynamicText = false;

    @Field
    private Boolean isDynamicTextWithButton = false;

    @Field
    private Double serviceFeePercentage;

    @Field
    private String inviteUrl;
}
