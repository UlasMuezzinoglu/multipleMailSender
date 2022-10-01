package com.ulas.springcoretemplate.service.user;

import com.mongodb.client.MongoDatabase;
import com.ulas.springcoretemplate.constant.ErrorConstants;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.repository.user.MailTemplateRepository;
import com.ulas.springcoretemplate.interfaces.service.other.EmailService;
import com.ulas.springcoretemplate.interfaces.service.user.MailTemplateService;
import com.ulas.springcoretemplate.model.common.ByIdRequest;
import com.ulas.springcoretemplate.model.common.PageRequest;
import com.ulas.springcoretemplate.model.entity.EmailTemplateEntity;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.model.request.CreateMailTemplateRequest;
import com.ulas.springcoretemplate.model.request.SendMailWithTemplateRequest;
import com.ulas.springcoretemplate.model.util.CustomPage;
import com.ulas.springcoretemplate.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ulas.springcoretemplate.constant.CollectionConstants.EMAIL_TEMPLATE;
import static com.ulas.springcoretemplate.constant.GeneralConstants.PAGE_SIZE;
import static com.ulas.springcoretemplate.constant.SuccessMessageConstants.SUCCESS;
import static com.ulas.springcoretemplate.util.MethodUtils.getActiveUser;

@Service
@RequiredArgsConstructor
public class MailTemplateServiceImpl implements MailTemplateService {

    private final MailTemplateRepository mailTemplateRepository;
    private final MongoDatabase mongoDatabase;
    private final EmailService emailService;


    @Override
    public String createMailTemplate(CreateMailTemplateRequest createMailTemplateRequest) {
        UserEntity activeUser = getActiveUser();

        EmailTemplateEntity emailTemplateEntity = new EmailTemplateEntity();
        emailTemplateEntity.setHtmlContent(createMailTemplateRequest.getHtmlContent());
        emailTemplateEntity.setRecipients(createMailTemplateRequest.getRecipients());
        emailTemplateEntity.setUserId(activeUser.getId());
        emailTemplateEntity.setSubject(createMailTemplateRequest.getSubject());

        mailTemplateRepository.save(emailTemplateEntity);

        return SUCCESS;
    }

    @Override
    public String sendMailWithTemplate(SendMailWithTemplateRequest sendMailWithTemplateRequest) {
        EmailTemplateEntity emailTemplateEntity =
                mailTemplateRepository.findById(sendMailWithTemplateRequest.getId()
                        , mongoDatabase, EMAIL_TEMPLATE, EmailTemplateEntity.class);

        UserEntity activeUser = getActiveUser();

        if (!emailTemplateEntity.getUserId().equals(activeUser.getId()))
            throw new CustomException(ErrorConstants.THIS_OPERATION_DOES_NOT_BELONG_TO_THIS_USER);

        emailService.sendWithTemplate(emailTemplateEntity.getRecipients(), emailTemplateEntity.getHtmlContent()
                , emailTemplateEntity.getSubject(), sendMailWithTemplateRequest.getIncludeRecipientsCC(),null);
        return SUCCESS;
    }

    @Override
    public CustomPage<Document> getAllMyTemplates(PageRequest pageRequest) {
        UserEntity activeUser = getActiveUser();
        Document match = new Document("userId",activeUser.getId());

        return new CustomPage<>(mongoDatabase.getCollection(EMAIL_TEMPLATE).aggregate(
                List.of(
                        new Document("$match",match),
                        new Document("$addFields",new Document("id",new Document("$toString","$_id"))),
                        new Document("$project",new Document("_id",0)),
                        new Document("$sort", new Document(pageRequest.getSortBy(), pageRequest.getDirection().isDescending() ? -1 : 1)),
                        new Document("$skip", PAGE_SIZE * pageRequest.getPageNumber()),
                        new Document("$limit", PAGE_SIZE)
                )
        ).into(new ArrayList<>()), PageableUtils.getPageable(pageRequest.getPageNumber(), pageRequest.getSortBy(),
                pageRequest.getDirection()), mongoDatabase.getCollection(EMAIL_TEMPLATE).countDocuments(match));
    }
}
