package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelRepository {
    void save(Channel channel);
    Channel findById(String channelId);
    Channel findByChannelName(String channelName);
    List<Channel> findAllChannels();
    void updateChannel(String channelId);
    void deleteChannel(String channelId);
    boolean existsByChannelName(String channelName);
}
