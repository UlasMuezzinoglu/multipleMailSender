package com.ulas.springcoretemplate.interfaces.service.user;

import com.ulas.springcoretemplate.model.common.ByIdRequest;
import com.ulas.springcoretemplate.model.common.PageRequest;
import com.ulas.springcoretemplate.model.request.CreateMailTemplateRequest;
import com.ulas.springcoretemplate.model.request.SendMailWithTemplateRequest;
import com.ulas.springcoretemplate.model.util.CustomPage;
import org.bson.Document;


public interface MailTemplateService{

    String createMailTemplate(CreateMailTemplateRequest createMailTemplateRequest);

    String sendMailWithTemplate(SendMailWithTemplateRequest sendMailWithTemplateRequest);

    CustomPage<Document> getAllMyTemplates(PageRequest pageRequest);

}
