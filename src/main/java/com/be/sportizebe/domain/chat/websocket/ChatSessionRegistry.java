package com.be.sportizebe.domain.chat.websocket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

@Component
public class ChatSessionRegistry {

    public record Presence(Long roomId, Long userId, String nickname) {}

    private final ConcurrentMap<String, Presence> store = new ConcurrentHashMap<>();

    public void put(String sessionId, Long roomId, Long userId, String nickname) {
        store.put(sessionId, new Presence(roomId, userId, nickname));
    }

    public Presence remove(String sessionId) {
        return store.remove(sessionId);
    }

    public Presence get(String sessionId) {
        return store.get(sessionId);
    }
}