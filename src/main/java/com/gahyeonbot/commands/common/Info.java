package com.gahyeonbot.commands.common;

import com.gahyeonbot.commands.util.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Info extends AbstractCommand {
    private final List<ICommand> commands;

    public Info(List<ICommand> commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return Description.INFO_NAME;
    }

    @Override
    public String getDescription() {
        return Description.INFO_DESC;
    }

    @Override
    public String getDetailedDescription() {
        return Description.INFO_DETAIL;
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        logger.info("명령어 실행 시작: {}", getName());
        // 명령어 목록 임베드 생성
        var embed = EmbedUtil.createCommandListEmbed(commands, event);
        // 응답 전송
        ResponseUtil.replyEmbed(event, embed);
    }
}
