package com.ulas.springcoretemplate.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BannedCauseEnum {
    ABUSE("ABUSE"),
    INAPPROPRIATE_CONTENT("INAPPROPRIATE_CONTENT"),
    SUSPECT("SUSPECT"),
    FRAUD("FRAUD"),
    OTHER("OTHER");

    private String bannedCause;

    BannedCauseEnum(String bannedCause) {this.bannedCause = bannedCause;}

    @JsonValue
    public String getBannedCause() {
        return this.bannedCause;
    }
}
