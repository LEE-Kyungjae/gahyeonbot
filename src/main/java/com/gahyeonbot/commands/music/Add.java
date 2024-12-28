package com.gahyeonbot.commands.music;

import com.gahyeonbot.commands.util.ICommand;
import com.gahyeonbot.commands.util.Description;
import com.gahyeonbot.commands.util.ResponseUtil;
import com.gahyeonbot.service.MusicManagerService;
import com.gahyeonbot.service.StreamingService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.List;

public class Add implements ICommand {
    private final MusicManagerService musicManagerService;
    private final StreamingService streamingService;

    public Add(MusicManagerService musicManagerService, StreamingService streamingService) {
        this.musicManagerService = musicManagerService;
        this.streamingService = streamingService;
    }

    @Override
    public String getName() {
        return Description.ADD_NAME;
    }

    @Override
    public String getDescription() {
        return Description.ADD_DESC;
    }

    @Override
    public String getDetailedDescription() {
        return Description.ADD_DETAIL;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "노래정보", "노래 제목을 입력하세요.", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // 노래정보 옵션 가져오기
        String query = event.getOption("노래정보", "", OptionMapping::getAsString).trim();
        if (query.isEmpty()) {
            ResponseUtil.replyError(event, "유효한 노래 정보를 입력하세요.");
            return;
        }
        var guild = event.getGuild();

        if (guild == null) {
            ResponseUtil.replyError(event, "길드를 찾을 수 없습니다.");
            return;
        }

        var musicManager = musicManagerService.getOrCreateGuildMusicManager(guild);

        if (!musicManagerService.ensureConnectedToVoiceChannel(event, guild, musicManager)) {
            return;
        }

        // 스트리밍 URL 검색 및 재생
        String streamUrl = streamingService.getStreamUrl(query);
        if (streamUrl == null) {
            ResponseUtil.replyError(event, "스트리밍 URL을 찾을 수 없습니다.");
            return;
        }

        musicManagerService.loadAndPlay(event, streamUrl, musicManager, query);
    }
}
