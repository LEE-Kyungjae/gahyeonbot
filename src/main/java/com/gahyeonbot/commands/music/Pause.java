package com.gahyeonbot.commands.music;

import com.gahyeonbot.commands.util.Description;
import com.gahyeonbot.commands.util.ICommand;
import com.gahyeonbot.commands.util.ResponseUtil;
import com.gahyeonbot.commands.util.EmbedUtil;
import com.gahyeonbot.manager.music.GuildMusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Map;

public class Pause implements ICommand {

    private final Map<Long, GuildMusicManager> musicManagers;

    public Pause(Map<Long, GuildMusicManager> musicManagers) {
        this.musicManagers = musicManagers;
    }

    @Override
    public String getName() {
        return Description.PAUSE_NAME;
    }

    @Override
    public String getDescription() {
        return Description.PAUSE_DESC;
    }

    @Override
    public String getDetailedDescription() {
        return Description.PAUSE_DETAIL;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        var guild = event.getGuild();
        if (guild == null) {
            ResponseUtil.replyError(event, "길드 정보를 가져올 수 없습니다.");
            return;
        }

        GuildMusicManager musicManager = musicManagers.get(guild.getIdLong());
        if (musicManager == null || musicManager.getCurrentTrack() == null) {
            ResponseUtil.replyError(event, "현재 재생 중인 음악이 없습니다.");
            return;
        }

        if (musicManager.isPaused()) {
            ResponseUtil.replyError(event, "음악이 이미 일시정지 상태입니다.");
        } else {
            musicManager.pause(); // 일시정지
            ResponseUtil.replyEmbed(event, EmbedUtil.createPauseEmbed(event));
        }
    }
}
