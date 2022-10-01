package com.ulas.springcoretemplate.controller.user;

import com.ulas.springcoretemplate.interfaces.controller.user.MailTemplateController;
import com.ulas.springcoretemplate.interfaces.service.user.MailTemplateService;
import com.ulas.springcoretemplate.model.common.ByIdRequest;
import com.ulas.springcoretemplate.model.common.PageRequest;
import com.ulas.springcoretemplate.model.request.CreateMailTemplateRequest;
import com.ulas.springcoretemplate.model.request.SendMailWithTemplateRequest;
import com.ulas.springcoretemplate.model.util.CustomPage;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailTemplateControllerImpl implements MailTemplateController {

    private final MailTemplateService mailTemplateService;

    @Override
    public MailTemplateService getService() {
        return mailTemplateService;
    }

    @Override
    public String createTemplate(CreateMailTemplateRequest createMailTemplateRequest) {
        return getService().createMailTemplate(createMailTemplateRequest);
    }

    @Override
    public String sendMailWithTemplate(SendMailWithTemplateRequest sendMailWithTemplateRequest) {
        return getService().sendMailWithTemplate(sendMailWithTemplateRequest);
    }

    @Override
    public CustomPage<Document> getAllMyTemplates(PageRequest pageRequest) {
        return getService().getAllMyTemplates(pageRequest);
    }
}
