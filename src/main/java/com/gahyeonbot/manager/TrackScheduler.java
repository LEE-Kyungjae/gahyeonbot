package com.gahyeonbot.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    // 대기열에 트랙 추가
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }
    public List<AudioTrack> getQueue() {
        return List.copyOf(queue);
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }
    // 다음 트랙 재생
    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }
    public void clearQueue() {
        queue.clear();
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}
