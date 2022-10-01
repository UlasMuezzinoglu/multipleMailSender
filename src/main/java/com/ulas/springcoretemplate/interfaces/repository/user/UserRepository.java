package com.ulas.springcoretemplate.interfaces.repository.user;


import com.ulas.springcoretemplate.interfaces.repository.common.BaseRepository;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, String> {
    UserEntity findByPhoneNumber(String username);

    UserEntity findByGsmCountryCodeAndPhoneNumber(String gsmCountryCode, String phoneNumber);


    boolean existsByMyRefCode(String refCode);

    boolean existsByUsername(String username);


    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByGsmCountryCodeAndPhoneNumber(String gsmCode, String phoneNumber);


    boolean existsByEmail(String email);

    UserEntity findByUsername(String username);

    UserEntity findByMyRefCode(String invitedRefCode);

    boolean existsByUrl(String url);
}
