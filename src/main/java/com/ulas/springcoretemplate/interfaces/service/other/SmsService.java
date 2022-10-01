package com.ulas.springcoretemplate.interfaces.service.other;


import java.util.List;

public interface SmsService {
    void sendSmsToGsm(List<String> phoneNumber, String message);
}
