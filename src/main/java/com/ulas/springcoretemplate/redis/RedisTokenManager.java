package com.ulas.springcoretemplate.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RedisTokenManager {
    private final RedisStorageManager redis;

    @Autowired
    RedisTokenManager(final RedisStorageManager redisStorageManager) {
        this.redis = redisStorageManager;
    }

    public synchronized void registerJwtToken(String tokenId) {
       /*if (tokenId == null) return;
        if (redis.set.isMember(RedisMapName.TOKEN_ID.name(), tokenId)) {
            throw new CustomException(TOKEN_ALREADY_REGISTERED);
        } else {
            redis.set.add(RedisMapName.TOKEN_ID.name(), tokenId);
        }
        */
    }

    public synchronized void unregisterToken(String tokenId) {
        /*if (tokenId == null) return;
        if (redis.set.isMember(RedisMapName.TOKEN_ID.name(), tokenId)) {
            redis.set.remove(RedisMapName.TOKEN_ID.name(), tokenId);
        } else {
            throw new CustomException(INVALID_OPERATION);
        }*/
    }

    public boolean invalidTokenId(String tokenId) {
        return false;
        //return tokenId == null || !redis.set.isMember(RedisMapName.TOKEN_ID.name(), tokenId);
    }
}
