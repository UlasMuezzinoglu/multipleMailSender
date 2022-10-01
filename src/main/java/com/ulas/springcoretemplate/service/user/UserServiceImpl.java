package com.ulas.springcoretemplate.service.user;

import com.mongodb.client.MongoDatabase;
import com.ulas.springcoretemplate.enums.Role;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.service.other.JwtService;
import com.ulas.springcoretemplate.interfaces.service.user.UserService;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.mongo.NativeQuery;
import com.ulas.springcoretemplate.redis.RedisTokenManager;
import com.ulas.springcoretemplate.util.DateUtil;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ulas.springcoretemplate.constant.CollectionConstants.NOTIFICATION_SETTINGS;
import static com.ulas.springcoretemplate.constant.CollectionConstants.USERS;
import static com.ulas.springcoretemplate.constant.ErrorConstants.INVALID_OPERATION;
import static com.ulas.springcoretemplate.constant.SuccessMessageConstants.SUCCESS;
import static com.ulas.springcoretemplate.util.MethodUtils.getActiveUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final MongoDatabase mongoDatabase;
    private final MongoTemplate mongoTemplate;
    private final RedisTokenManager redisTokenManager;
    private final JwtService jwtService;


    @Override
    public <Res> Res getActiveUserProfile() {
        UserEntity activeUser = getActiveUser();
        updateLastSeenDate(activeUser.getId());
        return (Res) mongoDatabase.getCollection(USERS, Document.class)
                .aggregate(List.of(
                        new Document("$match", new Document("_id", new ObjectId(activeUser.getId()))),
                        NativeQuery.CONVERT_ID_TO_STRING(),
                        new Document("$lookup", new Document("from", NOTIFICATION_SETTINGS)
                                .append("localField", "id")
                                .append("foreignField", "userId")
                                .append("as", "notificationSettings")),
                        new Document("$unwind", new Document("path", "$notificationSettings").append("preserveNullAndEmptyArrays", true)),
                        new Document("$addFields", new Document("isNotificationActive", "$notificationSettings.isAllOpen")),
                        new Document("$project",
                                new Document("_id", 0)
                                        .append("id", 1)
                                        .append("firstName", 1)
                                        .append("lastName", 1)
                                        .append("phoneNumber", 1)
                                        .append("district", 1)
                                        .append("city", 1)
                                        .append("country", 1)
                                        .append("dateOfBirth", 1)
                                        .append("gender", 1)
                                        .append("myRefCode", 1)
                                        .append("isRegisterCompleted", 1)
                                        .append("gsmCountryCode", 1)
                                        .append("language", 1)
                                        .append("isNotificationActive", 1)
                        )
                ))
                .first();
    }

    private void updateLastSeenDate(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("lastSeenDate", DateUtil.getCurrentDate());
        mongoTemplate.updateFirst(query, update, UserEntity.class);
    }

    @Override
    public <Res> Res refreshToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.clearContext();
        UserEntity userEntity = getActiveUser();

        if (!authentication.getPrincipal().equals(userEntity.getUsername()))
            throw new CustomException(INVALID_OPERATION);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEntity.getUsername(), null, getAuthorities(userEntity.getRole()));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        redisTokenManager.unregisterToken(jwtService.getTokenId());
        String tokenId = UUID.randomUUID().toString();
        redisTokenManager.registerJwtToken(tokenId);

        JSONObject response = new JSONObject();
        String token = jwtService.generateTokenForLogin(authenticationToken.getName(), authenticationToken.getAuthorities(), tokenId);
        response.appendField("token", token);
        response.appendField("code", SUCCESS);

        return (Res) response;
    }

    public List<SimpleGrantedAuthority> getAuthorities(Role role) {

        if (role == null) return null;

        return role.getGrantedAuthorities();
    }
}
