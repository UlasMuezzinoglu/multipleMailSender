package com.ulas.springcoretemplate.interfaces.repository.user;

import com.ulas.springcoretemplate.interfaces.repository.common.BaseRepository;
import com.ulas.springcoretemplate.model.entity.DocumentEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends BaseRepository<DocumentEntity, String> {
    Optional<DocumentEntity> findByLanguageAndName(String language, String name);
}
