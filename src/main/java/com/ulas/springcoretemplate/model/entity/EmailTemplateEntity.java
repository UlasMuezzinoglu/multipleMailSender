package com.ulas.springcoretemplate.model.entity;

import com.ulas.springcoretemplate.model.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static com.ulas.springcoretemplate.constant.CollectionConstants.EMAIL_TEMPLATE;

@Getter
@Setter
@Document(collection = EMAIL_TEMPLATE)
public class EmailTemplateEntity extends BaseEntity {

    private String userId;

    private String htmlContent;

    private List<String> recipients;

    private String subject;
}
