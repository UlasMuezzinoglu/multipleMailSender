package com.ulas.springcoretemplate.interfaces.repository.user;

import com.ulas.springcoretemplate.interfaces.repository.common.BaseRepository;
import com.ulas.springcoretemplate.model.entity.LanguageEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends BaseRepository<LanguageEntity, String> {
    Optional<LanguageEntity> findByLanguage(String language);
}
