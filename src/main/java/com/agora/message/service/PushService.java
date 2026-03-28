package com.agora.message.service;

import org.springframework.stereotype.Service;

@Service
public class PushService {

    public void sendToUser(Long userId, String title, String body) {
        System.out.println("📨 Push enviado a " + userId +
                " | Título: " + title +
                " | Mensaje: " + body);
    }
}
