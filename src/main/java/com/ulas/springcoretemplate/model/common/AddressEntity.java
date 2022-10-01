package com.ulas.springcoretemplate.model.common;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class AddressEntity extends BaseEntity {
    @Field
    private String city;

    @Field
    private String district;

    @Field
    private String country;

    @Field
    private Double latitude;

    @Field
    private Double longitude;

    @Field
    private Double latitudeDelta;

    @Field
    private Double longitudeDelta;

    @Field
    private String shortPhysicalAddress;

    @Field
    private String longPhysicalAddress;
}
