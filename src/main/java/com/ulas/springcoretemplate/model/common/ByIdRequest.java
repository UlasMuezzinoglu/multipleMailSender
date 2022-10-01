package com.ulas.springcoretemplate.model.common;

import com.ulas.springcoretemplate.validator.CustomConstraint;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.ulas.springcoretemplate.constant.RegexConstants.BLACK_LIST;
import static com.ulas.springcoretemplate.constant.RegexConstants.MONGO_ID_CONSTRAINT;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ByIdRequest {
    @CustomConstraint(regexp = {BLACK_LIST, MONGO_ID_CONSTRAINT}, constraintErrorCode = "0003", nullMessage = "0003", regexErrorCode = "0003")
    @ApiParam(required = true)
    private String id;
}
