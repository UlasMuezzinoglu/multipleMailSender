package com.ulas.springcoretemplate.service.other;


import com.ulas.springcoretemplate.interfaces.service.other.EmailService;
import com.ulas.springcoretemplate.property.EmailProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;
    private final ResourceLoader resourceLoader;
    private final EmailProperties emailProperties;


    @Override
    public boolean send(String emailTo, String subject, String templateName, Map<String, String> attachmentNameAndPath, Map model) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            if (attachmentNameAndPath != null)
                for (Map.Entry<String, String> entry : attachmentNameAndPath.entrySet())
                    messageHelper.addAttachment(entry.getKey(), getResourceFile(entry.getValue()).getFile()); // fileName = "mmm", filePath = "static/mmm.png"

            Template t = freemarkerConfig.getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            messageHelper.setTo(emailTo);
            messageHelper.setFrom(emailProperties.getFrom(), emailProperties.getPersonal());
            messageHelper.setSubject(subject);
            messageHelper.setText(html, true);

            mailSender.send(message);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void sendWithTemplate(List<String> emails, String html, String subject, Boolean includeCC, Map<String, String> attachmentNameAndPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            if (attachmentNameAndPath != null)
                for (Map.Entry<String, String> entry : attachmentNameAndPath.entrySet())
                    messageHelper.addAttachment(entry.getKey(), getResourceFile(entry.getValue()).getFile()); // fileName = "mmm", filePath = "static/mmm.png"

            if (includeCC) {
                String[] emailAddreses = new String[emails.size()];
                messageHelper.setTo(emails.toArray(emailAddreses));
                messageHelper.setFrom("mail", "Project");
                messageHelper.setSubject(subject);
                messageHelper.setText(html, true);
                mailSender.send(message);
            } else {
                emails.forEach(item -> {
                    try {
                        messageHelper.setTo(item);
                        messageHelper.setFrom("mail", "Project");
                        messageHelper.setSubject(subject);
                        messageHelper.setText(html, true);

                        mailSender.send(message);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
        fmConfigFactoryBean.setTemplateLoaderPath("/templates/");

        return fmConfigFactoryBean;
    }

    @Override
    public Resource getResourceFile(String name) {
        return resourceLoader.getResource("classpath:" + name);
    }
}
