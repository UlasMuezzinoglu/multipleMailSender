package com.ulas.springcoretemplate.model.entity;


import com.ulas.springcoretemplate.model.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.ulas.springcoretemplate.constant.CollectionConstants.SYSTEM_PROPERTIES;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = SYSTEM_PROPERTIES)
public class SystemProperties extends BaseEntity {
    @Field
    private int defaultCode;
    @Field
    private int pageSize;
    @Field
    private double processingFee;
    @Field
    private double comissonFee;
    @Field
    private int sphereRadius;
    @Field
    private String defaultSortBy;
    @Field
    private double imageSizeLimit;
    @Field
    private double fileSizeLimit;
    @Field
    private String loginMessageTR;
    @Field
    private String loginMessageEN;
}
