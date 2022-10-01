package com.ulas.springcoretemplate.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum DocumentNameEnum {
    ABOUT("ABOUT"),
    KVKK("KVKK"),
    PRIVACY_POLICY("PRIVACY_POLICY");

    private String name;

    DocumentNameEnum(String name) {
        this.name = name;
    }

}
