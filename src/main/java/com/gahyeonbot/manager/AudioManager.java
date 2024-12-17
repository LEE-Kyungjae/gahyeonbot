package com.gahyeonbot.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

public class AudioManager {
    public static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

    static {
        AudioSourceManagers.registerRemoteSources(playerManager);
    }
}
