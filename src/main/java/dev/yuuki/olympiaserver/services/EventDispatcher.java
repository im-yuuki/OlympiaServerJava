package dev.yuuki.olympiaserver.services;

import dev.yuuki.olympiaserver.utils.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventDispatcher extends TextWebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(EventDispatcher.class);
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    private final Authorization authorization;

    public EventDispatcher(Authorization authorization) {
        this.authorization = authorization;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        List<String> token = session.getHandshakeHeaders().get("Authorization");
        if (token == null || token.isEmpty()) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        try {
            User user = authorization.validateUser(token.getFirst());
            sessions.add(session);
            logger.info("[%s] (%s) connected to gateway. ID: %s".formatted(
                    user.name,
                    Objects.requireNonNull(session.getRemoteAddress()).toString(),
                    session.getId()
            ));
        } catch (Exception e) {
            session.close(CloseStatus.POLICY_VIOLATION);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (sessions.remove(session)) logger.info("Session ID: %s disconnected. Status: %s".formatted(session.getId(), status));
    }

    public void broadcast(String message) {
        sessions.forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    logger.error("Failed to send message to client ID: %s".formatted(session.getId()));
                }
            }
        });
    }

}