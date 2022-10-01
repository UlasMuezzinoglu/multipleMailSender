package com.ulas.springcoretemplate.util;


import com.ulas.springcoretemplate.enums.NotificationStatusEnum;
import com.ulas.springcoretemplate.enums.NotificationsTypeEnum;
import com.ulas.springcoretemplate.interfaces.repository.user.PushNotificationRepository;
import com.ulas.springcoretemplate.model.entity.PushNotificationEntity;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.property.OnesignalProperties;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class NotificationUtils {
    @Autowired
    private OnesignalProperties oneSignal;
    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    /**
     * Sets this scanner's delimiting pattern to a pattern constructed from the specified String.
     * An invocation of this method of the form useDelimiter(pattern) behaves in exactly the same way as the invocation useDelimiter
     *
     * @param con          as HttpURLConnection The input stream in it is used to open the url link.
     * @param httpResponse as int used to compare status codes
     * @return returns the delimited result of the connection as String
     * @throws IOException
     */
    private static String mountResponseRequest(HttpURLConnection con, int httpResponse) throws IOException {

        String jsonResponse;

        if (httpResponse >= HttpURLConnection.HTTP_OK
                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
            Scanner scanner = new Scanner(con.getInputStream(), StandardCharsets.UTF_8);
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        } else {
            Scanner scanner = new Scanner(con.getErrorStream(), StandardCharsets.UTF_8);
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        }
        return jsonResponse;
    }

    public static String concatenate(List<String> items) {
        var s = new Object() {
            String s = "";
        };

        for (int i = 0; i < items.size(); i++)
            if (i + 1 == items.size()) {
                s.s += items.get(i);
                break;
            } else {
                s.s = s.s.concat(items.get(i));
                s.s = s.s.concat("/");
            }

        return s.s;
    }

    @SneakyThrows
    public void sendMessageWithoutSubtitle(JSONObject notification) {
        notification.put("app_id", oneSignal.getAPP_ID());
        notification.put("ios_sound", "notification_sound.wav");
        notification.put("sound", "notification");
        notification.put("android_channel_id", oneSignal.getANDROID_CHANNEL_ID());

        sendMessageToUser(notification.toString());
    }

    /**
     * this method sends notification with message to user
     *
     * @param message  as String notification message to be sent
     * @param ids      as String notification ids to be sent
     * @param action   as String notification action to be sent
     * @param data     as String notification data to be sent
     * @param title    as String notification title to be sent
     * @param subtitle as String notification subtitle to be sent
     * @param language as String notification language to be sent
     */
    @SneakyThrows
    public void sendMessage(
            String message, JSONArray ids, String action, JSONObject data,
            String title, String subtitle, String language) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("app_id", oneSignal.getAPP_ID());
        jsonObject.put("include_player_ids", ids);
        jsonObject.put("data", data);
        jsonObject.put("action", action);
        jsonObject.put("headings", new JSONObject().put(language, title));
        jsonObject.put("subtitle", new JSONObject().put(language, subtitle));
        jsonObject.put("contents", new JSONObject().put(language, message));

        sendMessageToUser(jsonObject.toString());
    }

    /**
     * this method sends notification to user but no subtitle and message.
     *
     * @param ids      as String notification ids to be sent
     * @param action   as String notification action to be sent
     * @param data     as String notification data to be sent
     * @param title    as String notification title to be sent
     * @param language as String notification language to be sent
     */
    public void sendMessageWithoutMessageAndSubtitle(String ids, String action, String data,
                                                     String title, String language) {
        String strJsonBody = "{"
                + "\"app_id\": \"" + oneSignal.getAPP_ID() + "\","
                + "\"include_player_ids\": " + ids + ","
                + "\"data\": {\"id\": \"" + data + "\"},"
                + "\"action\": \"" + action + "\","
                + "\"headings\": {\"" + language + "\": \"" + title + "\"},"
                + "\"contents\": {\"" + language + "\": \"" + title + "\"}"
                + "}";
        sendMessageToUser(strJsonBody);
    }

    /**
     * this method sends to notification message to user
     * his method is used to enable streaming of a HTTP request body without internal buffering,
     * when the content length is known in advance.
     *
     * @param strJsonBody notification body as string
     */
    private void sendMessageToUser(String strJsonBody) {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", oneSignal.getREST_API_KEY());
            con.setRequestMethod("POST");

            byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();

            jsonResponse = mountResponseRequest(con, httpResponse);


        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * this method provides transform a list to String base.
     *
     * @param ids as List<String> list of ids to be sent
     * @return list of string as String
     */
    public String fromListToString(final List<String> ids) {

        StringBuilder ss = new StringBuilder("[");

        for (int i = 0; i < ids.size(); i++)
            if (i + 1 == ids.size())
                ss.append("\"").append(ids.get(i)).append("\"");
            else
                ss.append("\"").append(ids.get(i)).append("\"").append(",");

        ss.append("]");
        return ss.toString();
    }

    /**
     * This method sends to login code to user as notification
     *
     * @param notificationId as String notification Id to be sent
     * @param code           as String code to be sent
     */
    public void sendLoginCode(String notificationId, String code) {
        String strJsonBody = "{"
                + "\"app_id\": \"" + oneSignal.getAPP_ID() + "\","
                + "\"include_player_ids\": " + fromListToString(Arrays.asList(notificationId)) + ","
                + "\"code\": \"" + code + "\","
                + "}";
        sendMessageToUser(strJsonBody);
    }

    @SneakyThrows
    public void createAndSaveNotificationEntity(UserEntity userEntity,
                                                NotificationsTypeEnum notificationsTypeEnum,
                                                String screenName,
                                                String dataKey,
                                                String dataValue,
                                                String message,
                                                String title,
                                                UserEntity actor) {

        PushNotificationEntity pushNotificationEntity = new PushNotificationEntity();
        pushNotificationEntity.setUserId(userEntity.getId());
        pushNotificationEntity.setNotificationsType(notificationsTypeEnum);
        pushNotificationEntity.setScreenName(screenName);
        pushNotificationEntity.setActorId(actor.getId());
        pushNotificationEntity.setMessage(message);
        pushNotificationEntity.setStatus(NotificationStatusEnum.UNREAD);
        pushNotificationEntity.setLanguage("en");
        pushNotificationEntity.setTitle(title);

        net.minidev.json.JSONObject data = new net.minidev.json.JSONObject();
        data.put(dataKey, dataValue);
        pushNotificationEntity.setData(data);
    }

    @SneakyThrows
    public void createAndSaveNotificationEntity(UserEntity userEntity,
                                                NotificationsTypeEnum notificationsTypeEnum,
                                                String screenName,
                                                String dataKeyForId,
                                                String dataKeyForUrl,
                                                String dataValueForId,
                                                String dataValueForUrl,
                                                String message,
                                                String title,
                                                UserEntity actor) {

        PushNotificationEntity pushNotificationEntity = new PushNotificationEntity();
        pushNotificationEntity.setUserId(userEntity.getId());
        pushNotificationEntity.setNotificationsType(notificationsTypeEnum);
        pushNotificationEntity.setScreenName(screenName);
        pushNotificationEntity.setActorId(actor.getId());

        net.minidev.json.JSONObject data = new net.minidev.json.JSONObject();
        data.put(dataKeyForId, dataValueForId);
        data.put(dataKeyForUrl, dataValueForUrl);

        pushNotificationEntity.setData(data);
        pushNotificationEntity.setMessage(message);
        pushNotificationEntity.setStatus(NotificationStatusEnum.UNREAD);
        pushNotificationEntity.setLanguage("en");
        pushNotificationEntity.setTitle(title);

        pushNotificationRepository.save(pushNotificationEntity);
    }
}
