package com.ulas.springcoretemplate.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateMailTemplateRequest {
    private String htmlContent;
    private List<String> recipients;
    private String subject;
}
