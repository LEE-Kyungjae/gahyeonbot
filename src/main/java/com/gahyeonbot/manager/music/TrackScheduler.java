package com.gahyeonbot.manager.music;

import net.dv8tion.jda.api.managers.AudioManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
음악 대기열 관리
음악 재생 자동 전환
이벤트 리스너로 트랙 종료 감지
대기열 초기화 및 상태 확인
*/
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final AudioManager audioManager; // JDA AudioManager

    public TrackScheduler(AudioPlayer player, AudioManager audioManager) {
        this.player = player;
        this.audioManager = audioManager;
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
        AudioTrack nextTrack = queue.poll();
        if (nextTrack != null) {
            player.startTrack(nextTrack, false);
        } else {
            if (audioManager != null && audioManager.getConnectedChannel() != null) {
                audioManager.closeAudioConnection(); // 보이스 채널 연결 종료
                System.out.println("보이스 채널 연결이 종료되었습니다.");
            }
        }
    }
    public void clearQueue() {
        queue.clear();
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        } else {
            System.out.println("트랙 종료: " + track.getInfo().title + " (이유: " + endReason + ")");
            nextTrack();
        }
    }
}