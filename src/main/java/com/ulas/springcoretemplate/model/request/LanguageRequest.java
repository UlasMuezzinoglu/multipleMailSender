package com.ulas.springcoretemplate.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LanguageRequest {
    @NotBlank(message = "0148")
    private String language;
}
