package TTT.RUSH.Basic.service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PartyChatWebSocketService extends TextWebSocketHandler {

    // 파티별로 WebSocket 세션 관리
    private static final Map<Long, Set<WebSocketSession>> partySessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long partyId = getPartyId(session);
        if (partyId == null) {
            session.close();
            return;
        }

        partySessions.computeIfAbsent(partyId, k -> Collections.synchronizedSet(new HashSet<>())).add(session);
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long partyId = getPartyId(session);
        System.out.println("수신된 파티 ID: " + partyId + ", 메시지: " + message.getPayload()); // 로그 추가
        if (partyId == null) {
            session.close();
            return;
        }

        Set<WebSocketSession> sessions = partySessions.get(partyId);
        System.out.println("연결된 세션 수: " + (sessions != null ? sessions.size() : 0)); // 세션 로그

        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(message.getPayload()));
                }
            }
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        Long partyId = getPartyId(session);
        if (partyId != null) {
            Set<WebSocketSession> sessions = partySessions.get(partyId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    partySessions.remove(partyId);
                }
            }
        }
    }

    private Long getPartyId(WebSocketSession session) {
        String uri = session.getUri().toString();
        System.out.println("Session URI: " + uri); // 로그 추가
        String[] parts = uri.split("/");
        try {
            return Long.parseLong(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            System.err.println("파티 ID 파싱 실패: " + e.getMessage());
            return null;
        }
    }

}
