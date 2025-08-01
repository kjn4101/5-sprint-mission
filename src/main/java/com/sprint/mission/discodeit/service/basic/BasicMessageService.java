package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public BasicMessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Override
    public void createMessage(String content, String channelName, String creatorUserId) {
        Message message = new Message(content, channelName, creatorUserId);
        messageRepository.save(message);

        User user = userService.findUserById(UUID.fromString(creatorUserId));
        String username = (user != null) ? user.getUsername() : "Unknown";

        System.out.println("메시지가 생성되었습니다: [" + channelName + "] " + username + ": " + message.getContent() + " (" + message.getMsgId() + ")");
    }

    @Override
    public Message findMessage(UUID msgId) {
        return messageRepository.findByMessageId(msgId);
    }

    @Override
    public List<Message> findAllMessages() {
        List<Message> messages = messageRepository.findAllMessages();
        return messages != null ? messages : new ArrayList<>();
    }

    @Override
    public void updateMessage(UUID msgId, String newMsg) {
        Message msg = messageRepository.findByMessageId(msgId);
        if (msg == null) {
            System.out.println("해당 ID의 메시지를 찾을 수 없습니다.");
            return;
        }
        msg.setContent(newMsg);
        messageRepository.updateMessage(msgId, newMsg);
        System.out.println("메시지가 수정되었습니다: " + msg);
    }

    @Override
    public void deleteMessage(UUID msgId) {
        Message removed = messageRepository.findByMessageId(msgId);
        if (removed == null) {
            System.out.println("삭제할 메시지를 찾을 수 없습니다.");
            return;
        }
        messageRepository.deleteMessage(msgId);
        System.out.println("메시지가 삭제되었습니다: " + removed);
    }
}
