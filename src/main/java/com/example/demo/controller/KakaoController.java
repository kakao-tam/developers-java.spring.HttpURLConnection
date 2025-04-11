package com.example.demo.controller;

import com.example.demo.service.KakaoApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class KakaoController {

    private final KakaoApiService kakaoApiService;

    public KakaoController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/authorize")
    public RedirectView authorize(@RequestParam(required = false) String scope) {
        return new RedirectView(kakaoApiService.getAuthUrl(scope));
    }

    @GetMapping("/redirect")
    public RedirectView handleRedirect(@RequestParam String code) {
        boolean isSuccess = kakaoApiService.handleAuthorizationCallback(code);
        return new RedirectView("/index.html?login=" + (isSuccess ? "success" : "error"));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        return kakaoApiService.getUserProfile();
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends() {
        return kakaoApiService.getFriends();
    }

    @GetMapping("/message")
    public ResponseEntity<?> sendMessage() {
        return kakaoApiService.sendMessage(kakaoApiService.createDefaultMessage());
    }

    @GetMapping("/friend-message")
    public ResponseEntity<?> sendMessageToFriend(@RequestParam String uuid) {
        return kakaoApiService.sendMessageToFriend(uuid, kakaoApiService.createDefaultMessage());
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return kakaoApiService.logout();
    }

    @GetMapping("/unlink")
    public ResponseEntity<?> unlink() {
        return kakaoApiService.unlink();
    }
}