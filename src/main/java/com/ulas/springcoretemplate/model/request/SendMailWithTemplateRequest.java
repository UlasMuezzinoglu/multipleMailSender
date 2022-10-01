package com.ulas.springcoretemplate.model.request;

import com.ulas.springcoretemplate.model.common.ByIdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMailWithTemplateRequest extends ByIdRequest {
    private Boolean includeRecipientsCC;
}
