package com.example.demo.service;

import com.example.demo.dto.KakaoTokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class KakaoApiService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.kauth-host}")
    private String kauthHost;

    @Value("${kakao.kapi-host}")
    private String kapiHost;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final ObjectMapper objectMapper;

    public KakaoApiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession();
    }

    public String getAuthUrl(String scope) {
        return UriComponentsBuilder
                .fromHttpUrl(kauthHost + "/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParamIfPresent("scope", scope != null ? java.util.Optional.of(scope) : java.util.Optional.empty())
                .build()
                .toUriString();
    }

    public boolean handleAuthorizationCallback(String code) {
        try {
            KakaoTokenResponse tokenResponse = getToken(code);
            if (tokenResponse != null) {
                saveAccessToken(tokenResponse.getAccess_token());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private KakaoTokenResponse getToken(String code) throws Exception {
        String params = String.format("grant_type=authorization_code&client_id=%s&code=%s",
                clientId, code);
        String response = makeRequest(kauthHost + "/oauth/token", "POST", params);
        return objectMapper.readValue(response, KakaoTokenResponse.class);
    }

    private void saveAccessToken(String accessToken) {
        getSession().setAttribute("access_token", accessToken);
    }

    private String getAccessToken() {
        return (String) getSession().getAttribute("access_token");
    }

    private void invalidateSession() {
        getSession().invalidate();
    }

    public ResponseEntity<?> getUserProfile() {
        try {
            String response = makeRequest(kapiHost + "/v2/user/me", "GET", null);
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> getFriends() {
        try {
            String response = makeRequest(kapiHost + "/v1/api/talk/friends", "GET", null);
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> sendMessage(String messageRequest) {
        try {
            String response = makeRequest(kapiHost + "/v2/api/talk/memo/default/send", "POST", messageRequest);
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> sendMessageToFriend(String uuid, String messageRequest) {
        try {
            String response = makeRequest(
                    kapiHost + "/v1/api/talk/friends/message/default/send?receiver_uuids=[" + uuid + "]", "POST",
                    messageRequest);
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> logout() {
        try {
            String response = makeRequest(kapiHost + "/v1/user/logout", "POST", null);
            invalidateSession();
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> unlink() {
        try {
            String response = makeRequest(kapiHost + "/v1/user/unlink", "POST", null);
            invalidateSession();
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public String createDefaultMessage() {
        return "template_object={\"object_type\":\"text\",\"text\":\"Hello, world!\",\"link\":{\"web_url\":\"https://developers.kakao.com\",\"mobile_web_url\":\"https://developers.kakao.com\"}}";
    }

    private String makeRequest(String urlString, String method, String body) throws Exception {
        String result = "";
        try {
            String response = "";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if (body != null) {
                System.out.println("param : " + body);
                conn.setDoOutput(true);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                bw.write(body);
                bw.flush();
            }
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            System.out.println("reqURL : " + urlString);
            System.out.println("method : " + method);
            System.out.println("Authorization : Bearer " + getAccessToken());
            InputStream stream = conn.getErrorStream();
            if (stream != null) {
                try (Scanner scanner = new Scanner(stream)) {
                    scanner.useDelimiter("\\Z");
                    response = scanner.next();
                }
                System.out.println("error response : " + response);
                return response;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            br.close();
        } catch (IOException e) {
            return e.getMessage();
        }
        return result;
    }
}