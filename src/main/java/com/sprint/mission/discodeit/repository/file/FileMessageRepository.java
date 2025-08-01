package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private final String DIRECTORY = "Message";
    private final String EXTENSION = ".ser";

    public FileMessageRepository() {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    @Override
    public void save(Message message) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(getMsgFile(message.getMsgId())))){
                oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Message findByMessageId(UUID msgId) {
        File file = getMsgFile(msgId);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("메시지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Message> findAllMessages() {
        File dir = new File(DIRECTORY);
        File[] files = dir.listFiles();
        List<Message> messages = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
                        messages.add((Message) ois.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("메시지 불러오기 중 오류가 발생했습니다: " + e.getMessage(), e);
                }
            }
        }
        return messages;
    }

    @Override
    public void updateMessage(UUID msgId, String newMsg) {
        Message message = findByMessageId(msgId);
        if (message != null) {
            message.setContent(newMsg);
            message.setUpdatedAt(System.currentTimeMillis());
            save(message);
        }
    }

    @Override
    public void deleteMessage(UUID msgId) {
        File file = getMsgFile(msgId);
        if (file.exists()) {
            if (!file.delete()) {
            throw new RuntimeException("메시지 삭제에 실패했습니다: " + msgId);
            }
        }
    }

    private File getMsgFile(UUID msgId) {
        return new File(DIRECTORY + File.separator + msgId.toString() + EXTENSION);
    }
}
