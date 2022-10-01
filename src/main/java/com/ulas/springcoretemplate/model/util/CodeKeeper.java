package com.ulas.springcoretemplate.model.util;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class CodeKeeper implements Serializable {
    private String encodedCode;
    private Date lastSentDate;
}
