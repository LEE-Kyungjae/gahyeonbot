package com.gahyeonbot.commands;

import com.gahyeonbot.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 커멘드를 사용하면 일정시간뒤에 접속한 보이스채널에 본인을포함한 멤버들을 모두 내보내는 커멘드
 */
public class KickAllVoiceMember implements ICommand {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public String getName() {
        return "outwith";
    }

    @Override
    public String getDescription() {
        return "지정한 시간 후에 해당 보이스채널에 입장하고 있는 모든 유저를 내보냅니다.";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.INTEGER, "minute", "분", true)
                .setMinValue(1)
                .setMaxValue(10000));
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int minute = Integer.parseInt(event.getOption("minute").getAsString());
        if (minute <= 0) {
            event.reply("시간은 양의 정수여야 합니다.").queue();
            return;
        }
        Member member = event.getMember();
        if (member == null) {
            event.reply("오류: 사용자 정보를 찾을 수 없습니다.").setEphemeral(true).queue();
            return;
        }
        String nickname = member.getUser().getEffectiveName();

        VoiceChannel voiceChannel = (VoiceChannel) member.getVoiceState().getChannel();
        if (voiceChannel == null) {
            event.reply(nickname + "님! 보이스 채널에 접속 중이 아니에요!").queue();
            return;
        }

        List<Member> members = voiceChannel.getMembers();

        event.deferReply().setContent(memberString(members) + "님을 " + minute + "분후에 내보낼예정").queue(); // 응답 지연
        scheduler.schedule(() -> {
            for (Member voice_member : members) {
                if (voice_member.getVoiceState().getChannel().equals(event.getMember().getVoiceState().getChannel())) {
                    voice_member.getGuild().kickVoiceMember(voice_member).queue(
                            success -> Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getDefaultChannel()).asTextChannel().sendMessage("보이스채널에있는 " + voice_member.getEffectiveName() + "님을 내보냈습니다.").queue(),
                            failure -> Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getDefaultChannel()).asTextChannel().sendMessage("오류가 발생했습니다 " + voice_member.getEffectiveName() + "님은 아마 보이스채널에 없으신거같아요" + failure.getMessage()).queue()
                    );
                }
            }
        }, minute, TimeUnit.MINUTES);
    }

    private String memberString(List<Member> members) {
        boolean cnt = false;
        String result = "";
        for (Member member : members) {
            String nickname = member.getUser().getEffectiveName();

            if (cnt = false) {
                result += nickname;
            } else {
                result += " " + nickname;
            }
            cnt = true;
        }
        result.replace(" ", ", ");
        return result;
    }
}
