package com.ulas.springcoretemplate.interfaces.service.user;

import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.model.request.NotificationIdSaveRequest;
import com.ulas.springcoretemplate.model.request.SetNotificationSettingsRequest;

public interface NotificationService {
    void createNotificationSettingsEntity(UserEntity userEntity);

    String saveNotificationId(NotificationIdSaveRequest notificationIdSaveRequest);

    void deleteNotificationIds(UserEntity activeUser);

    void deleteNotificationId(String notificationId);

    String setNotificationSettings(SetNotificationSettingsRequest setNotificationSettingsRequest);

    void assignNotificationSettings(UserEntity userEntity);

}
