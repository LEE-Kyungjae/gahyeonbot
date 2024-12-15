package com.gahyeonbot.commands;

import com.gahyeonbot.ICommand;
import net.dv8tion.jda.api.Permission;
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
        return "현재 이 보이스채널에 입장하고 있는 모든 유저를 지정시간에 내보냅니다. ";
    }

    @Override
    public String getDetailedDescription() {
        return "";
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
        // 명령어 실행 사용자의 권한 확인
        Member executor = event.getMember();
        // 사용자가 'VOICE_MOVE_OTHERS' 권한이 있는지 확인
        if (!executor.hasPermission(Permission.VOICE_MOVE_OTHERS)) {
            event.reply("이 명령어를 실행할 권한이 없습니다. 당신에게 'VOICE_MOVE_OTHERS' 권한이 필요합니다.").setEphemeral(true).queue();
            return;
        }
        String presetValue = event.getOption("preset") != null ? event.getOption("preset").getAsString() : null;
        String customValue = event.getOption("time") != null ? event.getOption("time").getAsString() : null;

        if (presetValue != null && customValue != null) {
            event.reply("오류: 'preset'과 'custom' 중 하나만 선택해야 합니다.").setEphemeral(true).queue();
            return;
        }
        if (presetValue == null && customValue == null) {
            event.reply("오류: 'preset' 또는 'custom' 옵션 중 하나를 입력해야 합니다.").setEphemeral(true).queue();
            return;
        }

        //String timeInput = "";
        //String timeInput = Objects.requireNonNull(event.getOption("time")).getAsString();
        int hours = 0;
        int minutes = 0;
        int totalMinutes = -1;
        if (presetValue != null) {
            totalMinutes = Integer.parseInt(presetValue); // 선택지에서 가져온 값은 이미 분 단위
            hours = totalMinutes / 60;
            minutes = totalMinutes % 60;
        } else if (customValue != null) {
            //timeInput = customValue;
            if (Integer.parseInt(customValue) >= 10000) {
                event.deferReply().setContent("잘못입력하셨습니다 HHMM/HMM/MM 형식으로 다시 입력해주세요").queue();
                return;
            }
            totalMinutes = calculateMinutes(customValue);
            if (totalMinutes == -1) {
                event.deferReply().setContent("잘못입력하셨습니다 HHMM/HMM/MM 형식으로 다시 입력해주세요").queue();
                return;
            }
            hours = totalMinutes / 60;
            minutes = totalMinutes % 60;
        }


        //totalMinutes = calculateMinutes(timeInput);
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
        String timeMessage = hours > 0
                ? (minutes > 0 ? hours + "시간 " + minutes + "분 후" : hours + "시간 후")
                : minutes + "분 후";

        event.deferReply().setContent(memberString(members) + "님을 " + timeMessage + "에 내보낼게요").queue(); // 응답 지연

        scheduledTask = scheduler.schedule(() -> {
            for (Member voice_member : members) {
                if (Objects.equals(Objects.requireNonNull(voice_member.getVoiceState()).getChannel(), Objects.requireNonNull(event.getMember().getVoiceState()).getChannel())) {
                    voice_member.getGuild().kickVoiceMember(voice_member).queue(
                            success -> Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getDefaultChannel()).asTextChannel().sendMessage("보이스채널에있는 " + voice_member.getEffectiveName() + "님을 내보냈습니다.").queue(),
                            failure -> Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getDefaultChannel()).asTextChannel().sendMessage("오류가 발생했습니다 " + voice_member.getEffectiveName() + "님은 아마 보이스채널에 없으신거같아요" + failure.getMessage()).queue()
                    );
                }
            }
        }, totalMinutes, TimeUnit.MINUTES);
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

    //시간계산 로직
    private int calculateMinutes(String timeInput) {
        // 입력값이 숫자인지 확인
        try {
            int time = Integer.parseInt(timeInput);

            // 음수이거나 너무 큰 값이면 에러
            if (time <= 0 || time > 2400) {
                return -1; // 에러 코드
            }

            // 네 자리일 경우: 시간(앞 2자리)과 분(뒤 2자리)
            if (timeInput.length() == 4) {
                int hour = time / 100; // 앞 2자리
                int minute = time % 100; // 뒤 2자리

                if (hour > 24 || minute >= 60) {
                    return -1; // 에러 코드
                }
                return hour * 60 + minute;
            }

            // 세 자리일 경우: 시간(앞자리)과 분(뒤 2자리)
            if (timeInput.length() == 3) {
                int hour = time / 100; // 첫 자리
                int minute = time % 100; // 뒤 2자리

                if (hour > 24 || minute >= 60) {
                    return -1; // 에러 코드
                }
                return hour * 60 + minute;
            }

            // 두 자리일 경우: 분
            if (timeInput.length() == 2) {
                int minute = time;
                if (minute >= 60) {
                    return minute; // 99분 같은 경우 처리
                }
                return minute;
            }

            // 한 자리일 경우: 시간
            if (timeInput.length() == 1) {
                int hour = time;
                return hour;
            }

            // 그 외는 에러 처리
            return -1;

        } catch (NumberFormatException e) {
            return -1; // 에러 코드
        }
    }

}
