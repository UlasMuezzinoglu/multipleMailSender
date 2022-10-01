package com.ulas.springcoretemplate.interfaces.repository.user;

import com.ulas.springcoretemplate.interfaces.repository.common.BaseRepository;
import com.ulas.springcoretemplate.model.entity.EmailTemplateEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface MailTemplateRepository extends BaseRepository<EmailTemplateEntity, String> {
}
