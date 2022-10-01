package com.ulas.springcoretemplate.model.entity;


import com.ulas.springcoretemplate.model.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.ulas.springcoretemplate.constant.CollectionConstants.CONTACT;

@Getter
@Setter
@Document(collection = CONTACT)
public class ContactEntity extends BaseEntity {
    @Field
    private String imageUrl;
    @Field
    private String description;
    @Field
    private String email;
    @Field
    private String address;
    @Field
    private String phoneNumber;
    @Field
    private String whatsappNumber;
    @Field
    private double longitude;
    @Field
    private double latitude;
    @Field
    private String businessName;
    @Field
    private String tradeName;
    @Field
    private String authPerson;
    @Field
    private String tradeRegisterNumber;
    @Field
    private String mersisNo;
}
