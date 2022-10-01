package com.ulas.springcoretemplate.config;

import com.ulas.springcoretemplate.enums.DocumentNameEnum;
import com.ulas.springcoretemplate.enums.NotificationStatusEnum;
import com.ulas.springcoretemplate.interfaces.repository.user.ConfigRepository;
import com.ulas.springcoretemplate.interfaces.repository.user.DocumentRepository;
import com.ulas.springcoretemplate.interfaces.repository.user.LanguageRepository;
import com.ulas.springcoretemplate.interfaces.repository.user.PushNotificationRepository;
import com.ulas.springcoretemplate.model.entity.*;
import com.ulas.springcoretemplate.property.AssetProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.ulas.springcoretemplate.constant.CollectionConstants.CONTACT;
import static com.ulas.springcoretemplate.constant.CollectionConstants.SYSTEM_PROPERTIES;
import static com.ulas.springcoretemplate.enums.NotificationsTypeEnum.ADVERTISEMENT;

@Configuration
@Component
@RequiredArgsConstructor
public class DBInitializer {
    private final MongoTemplate mongoTemplate;
    private final ConfigRepository configRepository;
    private final LanguageRepository languageRepository;
    private final AssetProperties assetProperties;
    private final DocumentRepository documentRepository;
    private final PushNotificationRepository pushNotificationRepository;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    //@PostConstruct
    void contactInitializer() {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setAddress("ADRES");
        contactEntity.setEmail("info@mail.com");
        contactEntity.setPhoneNumber("telefon");
        contactEntity.setWhatsappNumber("wp telefon");
        contactEntity.setLatitude(41.057690);
        contactEntity.setLongitude(28.981140);
        contactEntity.setTradeName("Şirket Adı");
        contactEntity.setAuthPerson("Ulaş Müezzinoğlu");
        contactEntity.setTradeRegisterNumber("11111-5");
        contactEntity.setMersisNo("mersis no");
        contactEntity.setBusinessName("business.com");

        if (mongoTemplate.count(new Query(), ContactEntity.class) == 0)
            mongoTemplate.save(contactEntity, CONTACT);
    }

    //@PostConstruct
    @SneakyThrows
    public void postConfigs() {
        if (configRepository.count() == 0) {
            ConfigEntity configEntity = new ConfigEntity();
            configEntity.setImageQuality(50);
            configEntity.setImageReSizeWidth(1600);
            configEntity.setImageReSizeHeight(900);
            configEntity.setIsForce(false);
            configEntity.setIsForceToHint(false);
            configEntity.setIsForceUpdate(false);
            configEntity.setIsMaintenance(false);
            configEntity.setOptionalUpdate(false);
            configEntity.setInviteUrl("https://www.domain.com/invite/");
            configEntity.setServiceFeePercentage(8D);
            configRepository.save(configEntity);
        }
    }

    @PostConstruct
    @SneakyThrows
    public void postLang() {
        if (languageRepository.count() == 0) {
            List<LanguageEntity> entities = prepareLanguageEntity();
            if (entities.size() > 0)
                languageRepository.saveAll(entities);
        }
    }

    private List<LanguageEntity> prepareLanguageEntity() throws ParseException {
        List<LanguageEntity> entities = new ArrayList<>();
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

            entities.add(languageEntity);
        }
        return entities;
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

    //@PostConstruct
    @SneakyThrows
    void postSystemVariables() {
        if (mongoTemplate.count(new Query(), SystemProperties.class) == 0) {
            SystemProperties systemProperties = new SystemProperties();
            systemProperties.setComissonFee(2.99);
            systemProperties.setDefaultCode(111111);
            systemProperties.setDefaultSortBy("id");
            systemProperties.setFileSizeLimit(2.0D);
            systemProperties.setPageSize(10);
            systemProperties.setProcessingFee(2.99);
            systemProperties.setLoginMessageTR("PROJE icin tek kullanimlik giris kodunuzdur. Bu kodu siz istemediyseniz, lutfen bu mesaji dikkate almayin.");
            systemProperties.setLoginMessageTR("It is your one-time access code for the PROJECT. If you did not request this code, please ignore this message.");
            systemProperties.setImageSizeLimit(1.5D);
            systemProperties.setSphereRadius(1000000);
            mongoTemplate.save(systemProperties, SYSTEM_PROPERTIES);
        }
    }

    //@PostConstruct
    public void doc() throws IOException {
        List<DocumentEntity> documentEntities = new ArrayList<>();

        if (documentRepository.count() == 0) {
            File[] files = new ClassPathResource("files/document").getFile().listFiles();

            net.minidev.json.parser.JSONParser jsonParser = new JSONParser(JSONParser.MODE_PERMISSIVE);

            assert files != null;
            for (File file : files)
                try (FileReader reader = new FileReader(file)) {

                    JSONObject obj = (JSONObject) jsonParser.parse(reader);
                    Map<String, String> data = new TreeMap<>();

                    obj.forEach((s, o) -> {
                        data.put(s, o.toString());
                    });

                    String language = obj.get("language").toString();

                    DocumentEntity documentEntityAboutUs = new DocumentEntity();
                    documentEntityAboutUs.setLanguage(language);
                    documentEntityAboutUs.setDoc(data.get("aboutUs"));
                    documentEntityAboutUs.setName(DocumentNameEnum.ABOUT.getName());

                    DocumentEntity documentEntityKvkk = new DocumentEntity();
                    documentEntityKvkk.setLanguage(language);
                    documentEntityKvkk.setDoc(data.get("kvkk"));
                    documentEntityKvkk.setName(DocumentNameEnum.KVKK.getName());

                    DocumentEntity documentEntityPrivacyPolicy = new DocumentEntity();
                    documentEntityPrivacyPolicy.setLanguage(language);
                    documentEntityPrivacyPolicy.setDoc(data.get("privacyPolicy"));
                    documentEntityPrivacyPolicy.setName(DocumentNameEnum.PRIVACY_POLICY.getName());

                    documentEntities.add(documentEntityAboutUs);
                    documentEntities.add(documentEntityKvkk);
                    documentEntities.add(documentEntityPrivacyPolicy);

                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }

            if (documentEntities.size() > 0)
                documentRepository.saveAll(documentEntities);
        }
    }

    //@PostConstruct
    private void postPushNotification() {
        if (pushNotificationRepository.count() == 0) {
            PushNotificationEntity pushNotificationEntity = new PushNotificationEntity();
            pushNotificationEntity.setUserId("userId");
            pushNotificationEntity.setLanguage("en");
            pushNotificationEntity.setNotificationsType(ADVERTISEMENT);
            pushNotificationEntity.setMessage("db init test");
            pushNotificationEntity.setStatus(NotificationStatusEnum.UNREAD);
            pushNotificationEntity.setTitle("db init test title");
            pushNotificationRepository.save(pushNotificationEntity);
        }
    }
}
