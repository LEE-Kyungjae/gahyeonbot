package com.gahyeonbot.commands.music;

import com.gahyeonbot.ICommand;
import com.gahyeonbot.config.Description;
import com.gahyeonbot.manager.AudioManager;
import com.gahyeonbot.manager.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Map;

public class Play implements ICommand {
    private final AudioPlayerManager playerManager = AudioManager.playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public Play(Map<Long, GuildMusicManager> musicManagers) {
        this.musicManagers = musicManagers;
    }

    @Override
    public String getName() {
        return Description.PLAY_NAME;
    }

    @Override
    public String getDescription() {
        return Description.PLAY_DESC;
    }

    @Override
    public String getDetailedDescription() {
        return Description.PLAY_DETAIL;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "query", "노래 제목을 입력하세요.", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String query = event.getOption("query").getAsString();
        Guild guild = event.getGuild();

        if (guild == null) {
            event.reply("길드를 찾을 수 없습니다.").setEphemeral(true).queue();
            return;
        }

        if (!guild.getAudioManager().isConnected()) {
            var voiceChannel = event.getMember().getVoiceState().getChannel();
            if (voiceChannel == null) {
                event.reply("먼저 보이스 채널에 입장하세요.").setEphemeral(true).queue();
                return;
            }
            guild.getAudioManager().openAudioConnection(voiceChannel);
        }

        GuildMusicManager musicManager = musicManagers.computeIfAbsent(
                guild.getIdLong(), id -> new GuildMusicManager(playerManager)
        );

        String searchQuery = "ytsearch:" + query;
        playerManager.loadItem(searchQuery, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (!musicManager.player.startTrack(track, true)) {
                    musicManager.scheduler.queue(track);
                    event.reply("🎵 대기열에 추가되었습니다: **" + track.getInfo().title + "**").queue();
                } else {
                    event.reply("🎵 재생을 시작합니다: **" + track.getInfo().title + "**").queue();
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getTracks().get(0);
                trackLoaded(firstTrack);
            }

            @Override
            public void noMatches() {
                event.reply("노래를 찾을 수 없습니다.").setEphemeral(true).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.reply("🚨 로딩 오류 발생: " + e.getMessage()).setEphemeral(true).queue();
            }
        });
    }
}