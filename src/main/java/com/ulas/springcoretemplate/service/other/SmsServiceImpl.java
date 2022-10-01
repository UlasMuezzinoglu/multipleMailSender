package com.ulas.springcoretemplate.service.other;


import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.interfaces.service.other.SmsService;
import com.ulas.springcoretemplate.property.SmsProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final LoggingBean LOG;
    private final SmsProperties smsProperties;

    @SneakyThrows
    @Async("threadPoolTaskExecutor")
    public void sendSmsToGsm(List<String> smsTo, String message) {
        URL url = new URL(smsProperties.getUrl());

        URLConnection urlConnection = url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) urlConnection;
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        OutputStream out = connection.getOutputStream();
        OutputStreamWriter wout = new OutputStreamWriter(out, StandardCharsets.UTF_8);

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' ?> " +
                "<mainbody> " +
                "<header>  " +
                "<company dil='TR'>Netgsm</company>   " +
                "<usercode>" + smsProperties.getUsername() + "</usercode> " +
                "<password>" + smsProperties.getPassword() + "</password>  " +
                "<type>1:n</type>  " +
                "<msgheader>" + smsProperties.getHeader() + "</msgheader>  " +
                "</header>  " +
                "<body>  " +
                "<msg>  " +
                "<![CDATA[" + message + "]]>  " +
                "</msg>  ");
        for (String number : smsTo) {
            if (number.startsWith("90")) {
                sb.append("<no>" + number.substring(2) + "</no>    ");
            } else if (number.startsWith("0")) {
                sb.append("<no>" + number.substring(1) + "</no>    ");
            } else {
                sb.append("<no>" + number + "</no>    ");
            }
        }
        sb.append("</body>  " +
                "</mainbody>");
        wout.write(sb.toString());
        wout.flush();
        out.close();
        wout.close();
        InputStream in = connection.getInputStream();
        in.close();
        out.close();
        connection.disconnect();

        LOG.activity.info("code is sent to {}", smsTo);
    }
}
