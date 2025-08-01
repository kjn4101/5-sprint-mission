package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final UserService userService;
    private final Map<String, Message> messages = new HashMap<>();

    public void saveMessageData(List<Message> messages) {
        try (FileOutputStream fos = new FileOutputStream("Message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Message> loadMessageData() {
        File file = new File("Message.ser");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public FileMessageService(UserService userService) {
        this.userService = userService;
        List<Message> loaded = loadMessageData();
        for (Message msg : loaded) {
            messages.put(msg.getMsgId().toString(), msg);
        }
    }

    @Override
    public void createMessage(String content, String channelName, String creatorUserId) {
        Message message = new Message(content, channelName, creatorUserId);
        messages.put(message.getMsgId().toString(), message);

        User user = userService.findUserById(UUID.fromString(creatorUserId));
        String username = user != null ? user.getUsername() : "Unknown";

        saveMessageData(new ArrayList<>(messages.values()));
        System.out.println("메시지가 생성되었습니다: [" + channelName + "] " + username + ": " + message.getContent() + " (" + message.getMsgId() + ")");
    }

    @Override
    public Message findMessage(UUID msgId) {
        return messages.get(msgId.toString());
    }

    @Override
    public List<Message> findAllMessages() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void updateMessage(UUID msgId, String newMsg) {
        Message msg = messages.get(msgId.toString());
        if (msg == null) {
            System.out.println("해당 ID의 메시지를 찾을 수 없습니다.");
            return;
        }

        msg.setContent(newMsg);
        saveMessageData(new ArrayList<>(messages.values()));
        System.out.println("메시지가 수정되었습니다: " + msg);
    }

    @Override
    public void deleteMessage(UUID msgId) {
        Message removed = messages.remove(msgId.toString());
        if (removed == null) {
            System.out.println("삭제할 메시지를 찾을 수 없습니다.");
            return;
        }
        saveMessageData(new ArrayList<>(messages.values()));
        System.out.println("메시지가 삭제되었습니다: " + removed);
    }

}
