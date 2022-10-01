package com.ulas.springcoretemplate.interfaces.repository.user;

import com.ulas.springcoretemplate.interfaces.repository.common.BaseRepository;
import com.ulas.springcoretemplate.model.entity.UrlEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlEntityRepository extends BaseRepository<UrlEntity,String> {
    void deleteAllByLocContains(String loc);
}
