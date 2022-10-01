package com.ulas.springcoretemplate.redis;


import com.ulas.springcoretemplate.model.entity.LanguageEntity;
import com.ulas.springcoretemplate.property.AssetProperties;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Component
public class OnApplication implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private RedisStorageManager redisStorageManager;
    @Autowired
    private AssetProperties assetProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadLanguages();
    }

    @Async("threadPoolTaskExecutor")
    @SneakyThrows
    public void loadLanguages() {
        JSONParser jsonParser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        InputStream inputStream = getInputStreamForLanguage();
        JSONArray array = (JSONArray) jsonParser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        for (Object item : array) {
            JSONObject obj = (JSONObject) item;
            JSONObject errors = (JSONObject) obj.get("ERROR");
            JSONObject success = (JSONObject) obj.get("SUCCESS");
            JSONObject texts = (JSONObject) obj.get("TEXT");

            String language = obj.get("language").toString();

            LanguageEntity languageEntity = new LanguageEntity();
            languageEntity.setError(errors);
            languageEntity.setLanguage(language);
            languageEntity.setSuccess(success);
            languageEntity.setTexts(texts);

            try {
                redisStorageManager.redisTemplate.delete(language);
            } catch (Exception e) {

            }
            redisStorageManager.list.rightPushAll(language, languageEntity);
        }
    }

    @SneakyThrows
    private InputStream getInputStreamForLanguage() {
        if (!activeProfile.equals("prod")) {
            return new ClassPathResource("files/language/language.json").getInputStream();
        }
        URL url = new URL(assetProperties.getLanguage());
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }
}
