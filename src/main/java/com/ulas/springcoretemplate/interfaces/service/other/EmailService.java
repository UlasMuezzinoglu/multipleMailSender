package com.ulas.springcoretemplate.interfaces.service.other;


import org.springframework.core.io.Resource;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.util.List;
import java.util.Map;

public interface EmailService {
    boolean send(String emailTo, String subject, String templateName, Map<String, String> attachmentNameAndPath, Map model);

    void sendWithTemplate(List<String> emails, String html , String subject, Boolean includeCC, Map<String, String> attachmentNameAndPath);


    FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration();

    Resource getResourceFile(String name);

    void sendMailFromAdmin(String message, String title, List<String> emails);
}
