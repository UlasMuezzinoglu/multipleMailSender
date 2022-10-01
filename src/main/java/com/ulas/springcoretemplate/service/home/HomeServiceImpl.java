package com.ulas.springcoretemplate.service.home;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.ulas.springcoretemplate.interfaces.service.home.DocumentService;
import com.ulas.springcoretemplate.interfaces.service.home.HomeService;
import com.ulas.springcoretemplate.model.common.PageRequest;
import com.ulas.springcoretemplate.model.entity.ConfigEntity;
import com.ulas.springcoretemplate.model.entity.ContactEntity;
import com.ulas.springcoretemplate.model.entity.UrlEntity;
import com.ulas.springcoretemplate.model.request.DocRequest;
import com.ulas.springcoretemplate.model.request.LanguageRequest;
import com.ulas.springcoretemplate.model.util.CustomPage;
import com.ulas.springcoretemplate.redis.RedisStorageManager;
import com.ulas.springcoretemplate.util.PageableUtils;
import com.ulas.springcoretemplate.util.SeoUtil;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ulas.springcoretemplate.constant.CollectionConstants.CONTACT;
import static com.ulas.springcoretemplate.constant.GeneralConstants.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final RedisStorageManager redisStorageManager;
    private final DocumentService documentService;
    private final MongoTemplate mongoTemplate;
    private final MongoDatabase database;
    private final SeoUtil seoUtil;


    @Override
    public <Res> Res getLanguage(LanguageRequest languageRequest) {
        List language = redisStorageManager.list.range
                (languageRequest.getLanguage(), 0, -1);
        if (language.size() == 0) return (Res) redisStorageManager.list.range
                ("tr-TR", 0, -1).get(0);
        else return (Res) language.get(0);
    }

    @Override
    public <Res> Res getDoc(DocRequest docRequest) {
        return documentService.getDoc(docRequest);
    }

    @Override
    public <Res> Res getConfigs() {
        ConfigEntity configEntity = mongoTemplate.findOne(new Query(), ConfigEntity.class);
        if (configEntity != null) return (Res) configEntity;
        JSONObject response = new JSONObject();
        response.appendField("imageQuality", configEntity.getImageQuality());
        response.appendField("imageReSizeWidth", configEntity.getImageReSizeWidth());
        response.appendField("imageReSizeHeight", configEntity.getImageReSizeHeight());
        response.appendField("isForceUpdate", configEntity.getIsForceUpdate());
        response.appendField("isForce", configEntity.getIsForce());
        response.appendField("isForceToHint", configEntity.getIsForceToHint());
        response.appendField("isMaintenance", configEntity.getIsMaintenance());
        response.appendField("optionalUpdate", configEntity.getOptionalUpdate());
        response.appendField("dynamicText", configEntity.getDynamicText());
        response.appendField("isDynamicText", configEntity.getIsDynamicText());
        response.appendField("isDynamicTextWithButton", configEntity.getIsDynamicTextWithButton());
        response.appendField("serviceFeePercentage", configEntity.getServiceFeePercentage());

        return (Res) response;
    }

    @Override
    public <Res> Res getSeoXml() {
        Document matchCriteria = new Document();
        MongoCursor responseList =
                database
                        .getCollection(
                                "urls",
                                UrlEntity.class
                        ).aggregate(Arrays.asList(
                                new Document("$match",
                                        matchCriteria
                                ),
                                new Document("$addFields",
                                        new Document("_id",
                                                new Document("$toString", "$_id")
                                        )
                                )
                        ))
                        .cursor();
        List<UrlEntity> objectList = new ArrayList<>();
        while (responseList.hasNext())
            objectList.add((UrlEntity) responseList.next());
        return (Res) seoUtil.generateXml(objectList);
    }

    @Override
    public <Res> Res getContact() {
        MongoCursor<ContactEntity> contactEntity = database.getCollection(CONTACT, ContactEntity.class)
                .aggregate(Arrays.asList(
                        new Document("$addFields",
                                new Document("_id",
                                        new Document("$toString", "$_id")
                                )
                        )
                )).cursor();

        return contactEntity.hasNext() ? (Res) contactEntity.next() : null;
    }

    @Override
    public <Res> Res getTestDummyData(PageRequest pageRequest) {
        List<Document> response = database.getCollection("dummies").aggregate(Arrays.asList(
                new Document("$sort", new Document(pageRequest.getSortBy(), pageRequest.getDirection().isDescending() ? -1 : 1)),
                new Document("$skip", PAGE_SIZE * pageRequest.getPageNumber()),
                new Document("$limit", PAGE_SIZE)
        )).into(new ArrayList<>());

        return (Res) new CustomPage<>(response,
                PageableUtils.getPageable(pageRequest.getPageNumber(), pageRequest.getSortBy(), pageRequest.getDirection()),
                database.getCollection("dummies").countDocuments());
    }
}
