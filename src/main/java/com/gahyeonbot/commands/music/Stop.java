package com.gahyeonbot.commands.music;

import com.gahyeonbot.ICommand;
import com.gahyeonbot.config.Description;
import com.gahyeonbot.manager.GuildMusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Map;

public class Stop implements ICommand {

    private final Map<Long, GuildMusicManager> musicManagers;

    public Stop(Map<Long, GuildMusicManager> musicManagers) {
        this.musicManagers = musicManagers;
    }

    @Override
    public String getName() {
        return Description.STOP_NAME;
    }

    @Override
    public String getDescription() {
        return Description.STOP_DESC;
    }

    @Override
    public String getDetailedDescription() {
        return Description.STOP_DETAIL;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        var guild = event.getGuild();

        if (guild == null) {
            event.reply("길드를 찾을 수 없습니다.").setEphemeral(true).queue();
            return;
        }

        var musicManager = musicManagers.get(guild.getIdLong());

        if (musicManager == null || musicManager.player.getPlayingTrack() == null) {
            event.reply("현재 재생 중인 트랙이 없습니다.").setEphemeral(true).queue();
            return;
        }

        musicManager.player.stopTrack();
        musicManager.scheduler.clearQueue();
        guild.getAudioManager().closeAudioConnection();

        event.reply("🎵 음악 재생을 중지하고 대기열을 초기화했습니다.").queue();
    }
}
