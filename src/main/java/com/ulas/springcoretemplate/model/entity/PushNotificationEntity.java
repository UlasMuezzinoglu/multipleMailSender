package com.ulas.springcoretemplate.model.entity;


import com.ulas.springcoretemplate.enums.NotificationStatusEnum;
import com.ulas.springcoretemplate.enums.NotificationsTypeEnum;
import com.ulas.springcoretemplate.model.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.ulas.springcoretemplate.constant.CollectionConstants.PUSH_NOTIFICATION;

@Getter
@Setter
@Document(collection = PUSH_NOTIFICATION)
@CompoundIndexes({
        @CompoundIndex(def = "{'userId':1}", name = "userId")
})
public class PushNotificationEntity extends BaseEntity {

    @Field
    @Indexed
    private String userId;

    @Field
    @Indexed
    private String actorId;

    @Field
    private JSONObject data;

    @Field
    private String title;

    @Field
    private String message;

    @Field
    private NotificationStatusEnum status;

    @Field
    private String screenName;

    @Field
    private String language;

    @Field
    private String logoUrl;

    @Field
    private NotificationsTypeEnum notificationsType = NotificationsTypeEnum.OTHER;
}
