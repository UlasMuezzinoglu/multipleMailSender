package com.ulas.springcoretemplate.model.request;

import com.ulas.springcoretemplate.enums.DocumentNameEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DocRequest {

    @NotNull(message = "0148")
    private String language;

    @NotNull(message = "0149")
    private DocumentNameEnum name;
}
