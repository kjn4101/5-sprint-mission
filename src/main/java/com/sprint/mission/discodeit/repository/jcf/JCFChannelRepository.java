package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<String, Channel> channels = new HashMap<>();


    @Override
    public void save(Channel channel) {
        channels.put(channel.getChannelName(), channel);
    }

    @Override
    public Channel findById(String channelId) {
        return null;
    }

    @Override
    public Channel findByChannelName(String channelName) {
        return channels.get(channelName);
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void updateChannel(String channelName) {
        Channel channel = channels.get(channelName);
        if (channel != null) {
            channel.update();
        }
    }

    @Override
    public void deleteChannel(String channelName) {
            channels.remove(channelName);
    }

    @Override
    public boolean existsByChannelName(String channelName) {
        return false;
    }
}
