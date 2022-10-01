package com.ulas.springcoretemplate.service.user;


import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.ulas.springcoretemplate.constant.ErrorConstants;
import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.enums.Role;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.repository.user.NotificationRepository;
import com.ulas.springcoretemplate.interfaces.repository.user.UserRepository;
import com.ulas.springcoretemplate.interfaces.service.user.NotificationService;
import com.ulas.springcoretemplate.model.entity.NotificationEntity;
import com.ulas.springcoretemplate.model.entity.NotificationSettingsEntity;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.model.request.NotificationIdSaveRequest;
import com.ulas.springcoretemplate.model.request.SetNotificationSettingsRequest;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ulas.springcoretemplate.constant.CollectionConstants.NOTIFICATION_SETTINGS;
import static com.ulas.springcoretemplate.constant.SuccessMessageConstants.SUCCESS;
import static com.ulas.springcoretemplate.util.MethodUtils.getActiveUser;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final LoggingBean logger;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final MongoDatabase mongoDatabase;


    @Async("threadPoolTaskExecutor")
    @Override
    public void createNotificationSettingsEntity(UserEntity userEntity) {

    }

    @Override
    public String saveNotificationId(NotificationIdSaveRequest notificationIdSaveRequest) {

        String notificationId = notificationIdSaveRequest.getNotificationId();
        NotificationEntity activeNotificationEntity =
                notificationRepository.findByNotificationId(notificationId);

        UserEntity activeUser = getActiveUser();

        if (activeNotificationEntity != null && activeUser == null) {
            logger.service.warn("User wanted to save notification data, but there's no " +
                    "active user in their request.");
            return SUCCESS;
        }

        if (activeNotificationEntity != null &&
                activeNotificationEntity.getUserId() != null &&
                activeUser.getNotificationId() != null &&
                activeUser.getNotificationId().equals(notificationIdSaveRequest.getNotificationId())) {
            logger.service.warn("User wanted to save notification data, but they already " +
                    "have that notification in their notifications.");
            return SUCCESS;
        }

        if (activeNotificationEntity != null) {
            activeNotificationEntity.setUserId(activeUser.getId());
            activeNotificationEntity.setUserType(activeUser.getRole());
            notificationRepository.save(activeNotificationEntity);
            activeUser.setNotificationId("");
            activeUser.setNotificationId(activeNotificationEntity.getNotificationId());
            userRepository.save(activeUser);

            logger.service.warn("User saved their notification data with id {} " +
                            "This notification already existed in the database, so not recreating it.",
                    activeNotificationEntity.getNotificationId());
            return SUCCESS;
        }

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setNotificationId(notificationIdSaveRequest.getNotificationId());
        notificationEntity.setUserType(activeUser != null ? activeUser.getRole() : Role.VISITOR);


        if (activeUser != null) {
            notificationEntity.setUserId(activeUser.getId());
            activeUser.setNotificationId("");
            activeUser.setNotificationId(notificationIdSaveRequest.getNotificationId());
            userRepository.save(activeUser);
            logger.service.warn("User saved their notification data with id {} " +
                            "This notification didn't exist in the database, so it was just created " +
                            "and put into the database.",
                    notificationIdSaveRequest.getNotificationId());
        }

        notificationRepository.save(notificationEntity);
        return SUCCESS;
    }

    @Override
    public void deleteNotificationIds(UserEntity activeUser) {
        List<NotificationEntity> notificationEntities = notificationRepository.findByUserId(activeUser.getId());
        notificationRepository.saveAll(notificationEntities.stream().map(notificationEntity -> {
            notificationEntity.setUserId(null);
            notificationEntity.setUserType(Role.VISITOR);
            return notificationEntity;
        }).collect(Collectors.toList()));
    }

    @Override
    public void deleteNotificationId(String notificationId) {
        NotificationEntity notificationEntity = notificationRepository.findByNotificationId(notificationId);
        notificationEntity.setUserId(null);
        notificationEntity.setRole(null);
        notificationEntity.setUserType(Role.VISITOR);
    }

    @Override
    public String setNotificationSettings(SetNotificationSettingsRequest setNotificationSettingsRequest) {
        UserEntity activeUser = getActiveUser();
        MongoCursor<NotificationSettingsEntity> notificationSettingsEntity = mongoDatabase.getCollection(NOTIFICATION_SETTINGS,
                NotificationSettingsEntity.class).aggregate(Arrays.asList(
                new Document("$addFields", new Document("_id", new Document("$toString", "$_id"))),
                new Document("$match", new Document("userId", activeUser.getId()))
        )).cursor();
        NotificationSettingsEntity entity = notificationSettingsEntity.hasNext()
                ? notificationSettingsEntity.next() : null;
        if (entity == null) throw new CustomException(ErrorConstants.NOT_FOUND);
        entity.setAllOpen(setNotificationSettingsRequest.isAllOpen());
        mongoTemplate.save(entity, NOTIFICATION_SETTINGS);
        return SUCCESS;
    }

    @Override
    public void assignNotificationSettings(UserEntity userEntity) {
        NotificationSettingsEntity notificationSettingsEntity = new NotificationSettingsEntity();
        notificationSettingsEntity.setNotificationId(userEntity.getNotificationId());
        notificationSettingsEntity.setUserId(userEntity.getId());
        mongoTemplate.save(notificationSettingsEntity, NOTIFICATION_SETTINGS);
    }
}

