package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);
    Message findByMessageId(UUID msgId);
    List<Message> findAllMessages();
    void updateMessage(UUID msgId, String newMsg);
    void deleteMessage(UUID msgId);
}
