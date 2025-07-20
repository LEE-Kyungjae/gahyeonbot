package com.gahyeonbot.core.audio;

import net.dv8tion.jda.api.managers.AudioManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 음악 트랙 스케줄링을 관리하는 클래스.
 * 음악 대기열 관리, 자동 재생 전환, 트랙 종료 이벤트 처리를 담당합니다.
 * 
 * @author GahyeonBot Team
 * @version 1.0
 */
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final TrackQueue trackQueue; // 대기열 관리 클래스
    private final AudioManager audioManager;

    /**
     * TrackScheduler 생성자.
     * 
     * @param player 오디오 플레이어
     * @param audioManager Discord 오디오 매니저
     */
    public TrackScheduler(AudioPlayer player, AudioManager audioManager) {
        this.player = player;
        this.audioManager = audioManager;
        this.trackQueue = new TrackQueue();
    }

    /**
     * 트랙을 대기열에 추가하거나 즉시 재생합니다.
     * 
     * @param track 추가할 트랙
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            trackQueue.addTrack(track);
        }
    }

    /**
     * 대기열의 모든 트랙을 반환합니다.
     * 
     * @return 대기열의 트랙 목록
     */
    public List<AudioTrack> getQueue() {
        return trackQueue.getTracks();
    }

    /**
     * 대기열이 비어있는지 확인합니다.
     * 
     * @return 대기열이 비어있으면 true
     */
    public boolean isQueueEmpty() {
        return trackQueue.isEmpty();
    }

    /**
     * 다음 트랙으로 넘어갑니다.
     */
    public void nextTrack() {
        AudioTrack nextTrack = trackQueue.pollTrack();
        if (nextTrack != null) {
            player.startTrack(nextTrack, false);
        } else {
            closeConnection();
        }
    }

    /**
     * 대기열을 초기화합니다.
     */
    public void clearQueue() {
        trackQueue.clear();
    }

    /**
     * 보이스 채널 연결을 종료합니다.
     */
    private void closeConnection() {
        if (audioManager != null && audioManager.getConnectedChannel() != null) {
            audioManager.closeAudioConnection();
            System.out.println("보이스 채널 연결이 종료되었습니다.");
        }
    }

    /**
     * 트랙 종료 이벤트를 처리합니다.
     * 
     * @param player 오디오 플레이어
     * @param track 종료된 트랙
     * @param endReason 종료 이유
     */
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
