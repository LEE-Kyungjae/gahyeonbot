package com.gahyeonbot.commands;

import com.gahyeonbot.ICommand;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Clean implements ICommand {
    @Override
    public String getName() {
        return "clean";
    }

    @Override
    public String getDescription() {
        return "채팅 삭제 커멘드입니다. 삭제할 줄수를 입력하세요";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.INTEGER, "line", "how many delete line?", true)
                .setMinValue(1)
                .setMaxValue(1000));
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MessageChannel channel = event.getChannel();
        OptionMapping command = event.getOption("line");
        if (command == null) {
            event.reply("삭제할 줄수를 다시 실행해주세요");
            return;
        }
        int line = command.getAsInt();

        channel.getHistory()
                .retrievePast(line)
                .queue(messages -> {channel.purgeMessages(messages);
                channel.sendMessage("채팅창을 청소했습니다").queue();
                });
    }
}