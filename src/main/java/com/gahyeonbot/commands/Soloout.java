package com.gahyeonbot.commands;

import com.gahyeonbot.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
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
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.STRING, "preset", "선택형 시간지정", false)
                .addChoice("1시간", "60")
                .addChoice("2시간", "120")
                .addChoice("3시간", "180")
                .addChoice("4시간", "240"));
        data.add(new OptionData(OptionType.INTEGER, "time", "직접 HHMM/HMM/MM 형식 시간 입력 (예: 130 → 1시간 30분)", false)
                .setMinValue(1)
                .setMaxValue(1000)
        );
        return data;
    }
    @Override
    public void execute(SlashCommandInteractionEvent event) {



        int minute = Integer.parseInt(event.getOption("time").getAsString());
        Member member = event.getMember();
        if (member == null) {
            event.reply("오류: 사용자 정보를 찾을 수 없습니다.").setEphemeral(true).queue();
            return;
        }
        VoiceChannel voiceChannel = (VoiceChannel) member.getVoiceState().getChannel();

        String nickname = member.getUser().getEffectiveName();

        if (voiceChannel == null) {
            event.reply(nickname +" 님! 보이스 채널에 접속 중이 아니에요! ").queue();
            return;
        }
        event.deferReply().setContent(minute+"분후에 내보낼예정").queue(); // 응답 지연
        scheduler.schedule(() -> {
            member.getGuild().kickVoiceMember(member).queue(
                    success -> event.getHook().sendMessage("보이스채널에있는 사용자를 내보냈습니다.").queue(), // 응답 지연 해제
                    failure -> event.getHook().sendMessage("오류가 발생했습니다 아마 보이스채널에 없으신거같아요" + failure.getMessage()).queue() // 응답 지연 해제
                    );
        },minute, TimeUnit.MINUTES);
    }
}
