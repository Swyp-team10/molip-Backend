package org.example.shallweeatbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VoteWebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(VoteWebSocketService.class);
    private final SimpMessagingTemplate messagingTemplate;

    public void sendVoteUpdate(Long teamBoardId, Map<String, Object> voteResults) {
        try {
            messagingTemplate.convertAndSend("/topic/vote/" + teamBoardId, voteResults);
            logger.info("WebSocket 메시지 전송 성공 - 팀 보드 ID: " + teamBoardId);
        } catch (Exception e) {
            logger.error("WebSocket 메시지 전송 실패 - 팀 보드 ID: " + teamBoardId + ", 에러 메시지: " + e.getMessage(), e);
        }
    }
}
