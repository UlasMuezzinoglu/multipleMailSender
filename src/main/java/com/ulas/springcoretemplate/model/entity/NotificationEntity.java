package com.ulas.springcoretemplate.model.entity;

import com.ulas.springcoretemplate.enums.Role;
import com.ulas.springcoretemplate.model.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.ulas.springcoretemplate.constant.CollectionConstants.NOTIFICATION;

@Getter
@Setter
@Document(collection = NOTIFICATION)
@CompoundIndexes({
        @CompoundIndex(def = "{'userId':1}", name = "userId")
})
public class NotificationEntity extends BaseEntity {

    @Field
    @Indexed
    private String userId;

    private String notificationId;

    @Field
    private Role userType = Role.VISITOR;

    @Field
    private Role role = Role.VISITOR;
}
