package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final String DIRECTORY = "Channel";
    private final String EXTENSION = ".ser";

    public FileChannelRepository() {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private File getChannelFileById(String channelId) {
        return new File(DIRECTORY, channelId + EXTENSION);
    }

    @Override
    public void save(Channel channel) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(getChannelFileById(channel.getChannelName())))) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public Channel findById(String channelId) {
        File file = getChannelFileById(channelId);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 불러오기 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public Channel findByChannelName(String channelName) {
        List<Channel> allChannels = findAllChannels();
        for (Channel ch : allChannels) {
            if (ch.getChannelName().equals(channelName)) {
                return ch;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAllChannels() {
        File dir = new File(DIRECTORY);
        File[] files = dir.listFiles();
        List<Channel> channels = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    channels.add((Channel) ois.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("채널 파일 읽어오기에 실패했습니다: " + file.getName());
                }
            }
        }
        return channels;
    }

    @Override
    public void updateChannel(String channelName) {
        Channel channel = findByChannelName(channelName);
        if (channel != null) {
            channel.update();
            save(channel);
        }
    }

    @Override
    public void deleteChannel(String channelId) {
        File file = getChannelFileById(channelId);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("채널 삭제에 실패했습니다: " + channelId);
            }
        } else {
            System.out.println("삭제할 채널이 존재하지 않습니다: " + channelId);
        }
    }

    @Override
    public boolean existsByChannelName(String channelName) {
        return findByChannelName(channelName) != null;
    }

}