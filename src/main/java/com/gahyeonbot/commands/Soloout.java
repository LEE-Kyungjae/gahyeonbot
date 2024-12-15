package com.gahyeonbot.commands;

import com.gahyeonbot.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Soloout implements ICommand {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public String getName() {
        return "soloout";
    }

    @Override
    public String getDescription() {
        return "지정한 시간 후에 보이스채널에 있는 사용자를 내보냅니다.";
    }

    @Override
    public String getDetailedDescription() {
        return  "preset: 기본 제공하는 선택형 옵션입니다. 1시간,2시간과같은 기본 내보낼 시간설정을 통해 지정할수있습니다.\n" +
                "time: 사용자가 직접 시간을 지정해 세세한 내보내기 시간지정을 할수있습니다. 1~4자리 숫자를 입력가능하며 \n" +
                "숫자4개-> 시간(2자리) + 분(1자리) / 숫자 3개-> 시간(1자리)+분(2자리) / 숫자 1,2개 -> 분 으로 처리합니다";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.STRING, "preset", "선택형 시간지정", false)
                .addChoice("1시간", "60")
                .addChoice("2시간", "120")
                .addChoice("3시간", "180")
                .addChoice("4시간", "240"));
        data.add(new OptionData(OptionType.INTEGER, "time", "직접 HHMM/HMM/MM 형식 시간 입력 (예: 130 → 1시간 30분)", false)
                .setMinValue(1)
                .setMaxValue(1000));
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int minute = Integer.parseInt(event.getOption("time").getAsString());
        var member = event.getMember();

        if (member == null || member.getVoiceState() == null || member.getVoiceState().getChannel() == null) {
            event.reply("오류: 보이스 채널에 접속 중이 아닙니다.").setEphemeral(true).queue();
            return;
        }

        var voiceChannel = member.getVoiceState().getChannel();
        var nickname = member.getEffectiveName();

        event.deferReply().setContent(minute + "분 후에 " + nickname + "님을 내보낼 예정입니다.").queue();

        scheduler.schedule(() -> {
            member.getGuild().kickVoiceMember(member).queue(
                    success -> event.getHook().sendMessage(nickname + "님을 보이스 채널에서 내보냈습니다.").queue(),
                    failure -> event.getHook().sendMessage("오류가 발생했습니다. " + failure.getMessage()).queue()
            );
        }, minute, TimeUnit.MINUTES);
    }
}