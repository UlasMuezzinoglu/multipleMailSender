package com.ulas.springcoretemplate.model.common;

import com.ulas.springcoretemplate.validator.CustomConstraint;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.ulas.springcoretemplate.constant.RegexConstants.BLACK_LIST;

@Getter
@Setter
public class PageRequest {
    @Min(value = 0, message = "0165")
    private int pageNumber;

    @CustomConstraint(min = 1, max = 100, regexp = {BLACK_LIST}, constraintErrorCode = "0167", nullMessage = "0166", regexErrorCode = "0168")
    @ApiParam(required = true)
    private String sortBy;

    @NotNull(message = "0169")
    private Sort.Direction direction;
}