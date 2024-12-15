package com.gahyeonbot.commands;

import com.gahyeonbot.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Info implements ICommand {
    private final List<ICommand> commands;

    public Info(List<ICommand> commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "명령어 사용법 안내서";
    }

    @Override
    public String getDetailedDescription() {
        return "";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("📜 명령어 목록")
                .setColor(Color.CYAN)
                .setDescription("아래는 봇이 지원하는 명령어 목록입니다.")
                .setFooter("가현봇 | 도움말", event.getJDA().getSelfUser().getEffectiveAvatarUrl());

        for (ICommand command : commands) {
            String detailedDescription = command.getDetailedDescription();
            String description = command.getDescription();

            String fieldValue = description;
            if (detailedDescription != null && !detailedDescription.isEmpty()) {
                fieldValue += "\n**상세설명:** " + detailedDescription;
            }

            embed.addField("/" + command.getName(), fieldValue, false);
        }

        event.replyEmbeds(embed.build()).queue();
    }
}