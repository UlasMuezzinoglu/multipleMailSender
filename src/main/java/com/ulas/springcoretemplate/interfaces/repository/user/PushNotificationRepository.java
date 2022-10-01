package com.ulas.springcoretemplate.interfaces.repository.user;

import com.ulas.springcoretemplate.interfaces.repository.common.BaseRepository;
import com.ulas.springcoretemplate.model.entity.PushNotificationEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationRepository extends BaseRepository<PushNotificationEntity, String> {
}
