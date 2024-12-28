package com.gahyeonbot.commands.out;

import com.gahyeonbot.commands.util.ICommand;
import com.gahyeonbot.commands.util.Description;
import com.gahyeonbot.commands.util.ResponseUtil;
import com.gahyeonbot.commands.util.EmbedUtil;
import com.gahyeonbot.manager.scheduler.LeaveSchedulerManager;
import com.gahyeonbot.models.Reservation;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Out implements ICommand {

    private final LeaveSchedulerManager schedulerManager;

    public Out(LeaveSchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    @Override
    public String getName() {
        return Description.OUT_NAME;
    }

    @Override
    public String getDescription() {
        return Description.OUT_DESC;
    }

    @Override
    public String getDetailedDescription() {
        return Description.OUT_DETAIL;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "preset", "선택형 시간 지정", false)
                        .addChoice("1시간", "60")
                        .addChoice("2시간", "120")
                        .addChoice("3시간", "180")
                        .addChoice("4시간", "240"),
                new OptionData(OptionType.INTEGER, "time", "직접 HHMM/HMM/MM 형식 시간 입력 (예: 130 → 1시간 30분)", false)
                        .setMinValue(1)
                        .setMaxValue(1000)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        var member = event.getMember();

        if (member == null || member.getVoiceState() == null || member.getVoiceState().getChannel() == null) {
            ResponseUtil.replyError(event, "오류: 보이스 채널에 접속 중이 아닙니다.");
            return;
        }

        // 시간 처리
        int minutes = resolveTime(event);
        if (minutes <= 0) {
            ResponseUtil.replyError(event, "시간을 올바르게 지정해주세요.");
            return;
        }

        var nickname = member.getEffectiveName();

        // 예약 생성
        var task = schedulerManager.scheduleTask(() -> {
            member.getGuild().kickVoiceMember(member).queue(
                    success -> ResponseUtil.sendMessageToChannel(event, nickname + "님을 보이스 채널에서 내보냈습니다."),
                    failure -> ResponseUtil.sendMessageToChannel(event, "오류 발생: " + failure.getMessage())
            );
        }, minutes, TimeUnit.MINUTES);

        // 예약 추가
        long reservationId = schedulerManager.addReservation(new Reservation(
                schedulerManager.generateId(),
                member.getIdLong(),
                nickname,
                member.getGuild().getIdLong(),
                task,
                "나가기 예약"
        ));

        // 응답 전송
        var embed = EmbedUtil.createReservationEmbed(reservationId, nickname, minutes);
        ResponseUtil.replyEmbed(event, embed);
    }

    private int resolveTime(SlashCommandInteractionEvent event) {
        var presetOption = event.getOption("preset");
        var timeOption = event.getOption("time");

        if (presetOption != null) {
            return Integer.parseInt(presetOption.getAsString());
        }
        if (timeOption != null) {
            return timeOption.getAsInt();
        }
        return -1; // 시간 미지정
    }
}
