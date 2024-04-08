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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 커멘드를 사용하면 일정시간뒤에 접속한 보이스채널에 본인을포함한 멤버들을 모두 내보내는 커멘드
 */
public class Outwith implements ICommand {
    private ScheduledFuture<?> scheduledTask;
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
        data.add(new OptionData(OptionType.STRING, "time", "내보낼 시간을 선택하세요", true)
                .addChoice("3시간", "180")
                .addChoice("2시간30분", "150")
                .addChoice("2시간", "120")
                .addChoice("1시간30분", "90")
                .addChoice("1시간", "60")
                .addChoice("45분", "45")
                .addChoice("30분", "30")
                .addChoice("10분", "10")
                .addChoice("5분", "5")
                .addChoice("1분", "1")
        );
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int minute = Integer.parseInt(Objects.requireNonNull(event.getOption("time")).getAsString());
        Member member = event.getMember();
        if (member == null) {
            event.reply("오류: 사용자 정보를 찾을 수 없습니다.").setEphemeral(true).queue();
            return;
        }
        String nickname = member.getUser().getEffectiveName();

        VoiceChannel voiceChannel = (VoiceChannel) Objects.requireNonNull(member.getVoiceState()).getChannel();
        if (voiceChannel == null) {
            event.reply(nickname + "님! 보이스 채널에 접속 중이 아니에요!").queue();
            return;
        }

        List<Member> members = voiceChannel.getMembers();

        event.deferReply().setContent(memberString(members) + "님을 " + minute + "분후에 내보낼예정").queue(); // 응답 지연
        scheduledTask = scheduler.schedule(() -> {
            for (Member voice_member : members) {
                if (Objects.equals(Objects.requireNonNull(voice_member.getVoiceState()).getChannel(), Objects.requireNonNull(event.getMember().getVoiceState()).getChannel())) {
                    voice_member.getGuild().kickVoiceMember(voice_member).queue(
                            success -> Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getDefaultChannel()).asTextChannel().sendMessage("보이스채널에있는 " + voice_member.getEffectiveName() + "님을 내보냈습니다.").queue(),
                            failure -> Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getDefaultChannel()).asTextChannel().sendMessage("오류가 발생했습니다 " + voice_member.getEffectiveName() + "님은 아마 보이스채널에 없으신거같아요" + failure.getMessage()).queue()
                    );
                }
            }
        }, minute, TimeUnit.MINUTES);
    }

    private String memberString(List<Member> members) {
        if (members.isEmpty()) {
            return "";
        }

        StringBuilder resultBuilder = new StringBuilder();
        boolean isFirst = true;

        for (Member member : members) {
            String nickname = member.getUser().getEffectiveName();
            if (isFirst) {
                resultBuilder.append(nickname);
                isFirst = false;
            } else {
                resultBuilder.append(", ").append(nickname);
            }
        }

        return resultBuilder.toString();
    }

    public void setScheduledTask(ScheduledFuture<?> scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    public ScheduledFuture<?> getScheduledTask() {
        return scheduledTask;
    }
}
