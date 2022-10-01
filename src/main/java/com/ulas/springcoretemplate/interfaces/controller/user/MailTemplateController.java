package com.ulas.springcoretemplate.interfaces.controller.user;

import com.ulas.springcoretemplate.interfaces.controller.common.BaseController;
import com.ulas.springcoretemplate.interfaces.service.user.MailTemplateService;
import com.ulas.springcoretemplate.model.common.ByIdRequest;
import com.ulas.springcoretemplate.model.common.PageRequest;
import com.ulas.springcoretemplate.model.request.CreateMailTemplateRequest;
import com.ulas.springcoretemplate.model.request.SendMailWithTemplateRequest;
import com.ulas.springcoretemplate.model.util.CustomPage;
import io.swagger.annotations.Api;
import org.bson.Document;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Api(tags = "MailTemplateController", value = "Mail Template Controller")
public interface MailTemplateController extends BaseController<MailTemplateService> {

    @PostMapping("user/createTemplate")
    String createTemplate(@Valid @ModelAttribute CreateMailTemplateRequest createMailTemplateRequest);

    @PostMapping("user/sendMailWithTemplate")
    String sendMailWithTemplate(@Valid @ModelAttribute SendMailWithTemplateRequest sendMailWithTemplateRequest);

    @PostMapping("user/getAllMyTemplates")
    CustomPage<Document> getAllMyTemplates(@Valid @ModelAttribute PageRequest pageRequest);


}
