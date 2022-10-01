package com.ulas.springcoretemplate.interfaces.repository.user;


import com.ulas.springcoretemplate.interfaces.repository.common.BaseRepository;
import com.ulas.springcoretemplate.model.entity.NotificationEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends BaseRepository<NotificationEntity, String> {
    NotificationEntity findByNotificationId(String notificationId);

    List<NotificationEntity> findByUserId(String userId);

}
