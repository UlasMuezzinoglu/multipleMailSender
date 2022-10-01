package com.ulas.springcoretemplate.model.entity;

import com.ulas.springcoretemplate.model.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.ulas.springcoretemplate.constant.CollectionConstants.NOTIFICATION_SETTINGS;

@Getter
@Setter
@Document(collection = NOTIFICATION_SETTINGS)
public class NotificationSettingsEntity extends BaseEntity {

    @Indexed
    private String userId;

    @Field
    private String notificationId;

    @Field
    private boolean isAllOpen = true;
}
