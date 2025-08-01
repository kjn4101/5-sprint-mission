package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageService messageService;

    public BasicChannelService(ChannelRepository channelRepository, MessageService messageService) {
        this.channelRepository = channelRepository;
        this.messageService = messageService;
    }

    @Override
    public void createChannel(String channelName, String ownerUserId) {
        Channel existing = channelRepository.findByChannelName(channelName);
        if (existing != null) {
            System.out.println("이미 존재하는 채널명입니다: " + channelName);
            return;
        }
        Channel newChannel = new Channel(channelName, ownerUserId);
        channelRepository.save(newChannel);
        System.out.println("채널이 생성되었습니다: " + newChannel);
    }

    @Override
    public Channel findByChannelName(String channelName) {
        Channel channel = channelRepository.findByChannelName(channelName);
        if (channel == null) {
            System.out.println("채널이 존재하지 않습니다: " + channelName);
            return null;
        }
        System.out.println("채널이 조회되었습니다: " + channel);
        return channel;
    }

    @Override
    public List<Channel> findAllChannels() {
        List<Channel> channels = channelRepository.findAllChannels();
        if (channels == null || channels.isEmpty()) {
            System.out.println("등록된 채널이 없습니다.");
            return new ArrayList<>();
        }
        System.out.println("전체 채널 목록:");
        for (Channel ch : channels) {
            System.out.println(ch);
        }
        return channels;
    }

    @Override
    public void updateChannel(String channelId) {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            System.out.println("수정할 채널이 존재하지 않습니다: " + channelId);
            return;
        }
        channel.setUpdatedAt(System.currentTimeMillis());
        channelRepository.updateChannel(channelId);
        System.out.println("채널이 수정되었습니다: " + channel);
    }

    @Override
    public void deleteChannel(String channelId) {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            System.out.println("삭제할 채널이 존재하지 않습니다: " + channelId);
            return;
        }
        List<Message> relatedMessages = messageService.findAllMessages().stream()
                .filter(msg -> msg.getChannelName().equals(channel.getChannelName()))
                .collect(Collectors.toList());
        for (Message msg : relatedMessages) {
            messageService.deleteMessage(msg.getMsgId());
        }
        channelRepository.deleteChannel(channelId);
        System.out.println("채널과 관련된 메시지들이 모두 삭제되고 채널이 삭제되었습니다: " + channel.getChannelName());
    }
}