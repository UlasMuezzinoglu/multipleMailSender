package com.ulas.springcoretemplate.interfaces.mapper;


import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.model.request.CompleteRegisterRequest;
import com.ulas.springcoretemplate.model.request.RegisterRequest;
import com.ulas.springcoretemplate.model.request.UpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    default UserEntity toEntity(CompleteRegisterRequest completeRegisterRequest, UserEntity userEntity) {
        if (completeRegisterRequest == null) {
            return null;
        }
        userEntity.setFirstName(completeRegisterRequest.getFirstName());
        userEntity.setLastName(completeRegisterRequest.getLastName());
        userEntity.setDateOfBirth(completeRegisterRequest.getDateOfBirth());
        userEntity.setEmail(completeRegisterRequest.getEmail());
        userEntity.setGender(completeRegisterRequest.getGender());

        return userEntity;

    }

    UserEntity toEntity(RegisterRequest registerRequest);

    void update(@MappingTarget UserEntity entity, UpdateUserRequest updateUserRequest);
}
