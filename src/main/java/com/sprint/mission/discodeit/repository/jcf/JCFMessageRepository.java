package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<String, Message> messages = new HashMap<>();

    @Override
    public void save(Message message) {
        messages.put(message.getContent(), message);
    }

    @Override
    public Message findByMessageId(UUID msgId) {
        return messages.get(msgId);
    }

    @Override
    public List<Message> findAllMessages() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void updateMessage(UUID msgId, String newMsg) {
        

    }

    @Override
    public void deleteMessage(UUID msgId) {

    }
}
