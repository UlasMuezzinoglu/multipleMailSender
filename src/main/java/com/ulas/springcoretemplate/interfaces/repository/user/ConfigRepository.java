package com.ulas.springcoretemplate.interfaces.repository.user;

import com.ulas.springcoretemplate.interfaces.repository.common.BaseRepository;
import com.ulas.springcoretemplate.model.entity.ConfigEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends BaseRepository<ConfigEntity, String> {
}
