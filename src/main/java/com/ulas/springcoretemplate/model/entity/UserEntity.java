package com.ulas.springcoretemplate.model.entity;

import com.ulas.springcoretemplate.enums.BannedCauseEnum;
import com.ulas.springcoretemplate.enums.Gender;
import com.ulas.springcoretemplate.enums.Role;
import com.ulas.springcoretemplate.enums.Status;
import com.ulas.springcoretemplate.model.common.AddressEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

import static com.ulas.springcoretemplate.constant.CollectionConstants.USERS;

@Getter
@Setter
@Document(value = USERS)
@CompoundIndexes({
        @CompoundIndex(def = "{'username':1, 'status':1}", name = "username_status"),
        @CompoundIndex(def = "{'phoneNumber':1, 'gsmCountryCode':1}", name = "phoneNumber_gsmCountryCode")
})public class UserEntity extends AddressEntity {

    @Field
    private String username;

    @Field
    private Role role = Role.USER;

    @Field
    private String firstName;

    @Field
    private String lastName;

    @Field
    private Date dateOfBirth;

    @Field
    @Indexed(unique = true)
    private String phoneNumber;

    @Field
    private String email;

    @Field
    private String language = "tr-TR";

    @Field
    private String identityNumber;

    @Field
    private boolean isPhoneNumberVerified = false;

    @Field
    private Status status = Status.ACTIVE;

    @Field
    private BannedCauseEnum bannedCause;

    @Field
    private String myRefCode;

    @Field
    private String invitedRefCode;

    @Field
    private String profileImageUrl;

    @Field
    private Gender gender = Gender.OTHER;

    @Field
    private Boolean opportunityAndCampaigns;

    @Field
    private Boolean kvkk;

    @Field
    private boolean isRegisterCompleted = false;

    @Field
    private String device;

    @Field
    private String notificationId;

    @Field
    private String url;

    @Field
    private String gsmCountryCode = "90";

    @Field
    private String deleteReason;

    @Field
    private Date lastSeenDate;
}
