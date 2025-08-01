package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    public void saveChannelData(List<Channel> channel) {
        try (FileOutputStream fos = new FileOutputStream("Channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
             ){
            oos.writeObject(channel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Channel> loadChannelData() {
        File file = new File("Channel.ser");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileInputStream fis = new FileInputStream("Channel.ser");
        ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public FileChannelService() {
        List<Channel> loaded = loadChannelData();
        for (Channel ch : loaded) {
            channels.put(ch.getChannelName(), ch);
        }
    }

    private final Map<String, Channel> channels = new HashMap<>();

    @Override
    public void createChannel(String channelName, String ownerUserId) {
        if (channels.containsKey(channelName)) {
            System.out.println("이미 존재하는 채널명입니다: " + channelName);
            return;
        }
        Channel newChannel = new Channel(channelName, ownerUserId);
        channels.put(channelName, newChannel);
        saveChannelData(new ArrayList<>(channels.values()));
        System.out.println("채널이 생성되었습니다: " + newChannel);
    }

    @Override
    public Channel findByChannelName(String channelName) {
        Channel ch = channels.get(channelName);
        if (ch == null) {
            System.out.println("채널이 존재하지 않습니다: " + channelName);
            return null;
        }
        System.out.println("채널이 조회되었습니다: " + ch);
        return ch;
    }

    @Override
    public List<Channel> findAllChannels() {
        if (channels.isEmpty()) {
            System.out.println("등록된 채널이 없습니다.");
            return Collections.emptyList();
        }
        System.out.println("전체 채널 목록:");
        for (Channel ch : channels.values()) {
            System.out.println(ch);
        }
        return new ArrayList<>(channels.values());
    }

    @Override
    public void updateChannel(String channelName) {
        Channel ch = channels.get(channelName);
        if (ch == null) {
            System.out.println("수정할 채널이 존재하지 않습니다: " + channelName);
            return;
        }
        ch.setUpdatedAt(System.currentTimeMillis());
        saveChannelData(new ArrayList<>(channels.values()));
        System.out.println("채널이 수정되었습니다: " + ch);
    }

    @Override
    public void deleteChannel(String channelName) {
        if (channels.containsKey(channelName)) {
            channels.remove(channelName);
            saveChannelData(new ArrayList<>(channels.values()));
            System.out.println("채널이 삭제되었습니다: " + channelName);
        } else {
            System.out.println("삭제할 채널이 존재하지 않습니다: " + channelName);
        }
    }
}
