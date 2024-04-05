package com.gahyeonbot.listeners;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class EventListeners extends ListenerAdapter {

    /**
     * 메시지 반응이모지 리스너
     */
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();
        assert user != null;
        String message = user.getAsMention() + "reacted to a message with " + emoji + "in the " + channelMention + "channel!";
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel().sendMessage(message).queue();

    }

    /**
     * 텍스트채널 메시지 수신리스너
     * 봇의 메시지일 경우 무시한다.
     * 이벤트가 발생한 채널에 메시지를 회신한다.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();
        if (message.contains("ping")) {
            event.getChannel().sendMessage("pong2").queue();
        }
    }
    /**
     * 서버 입장 리스너
     */
    //
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        User user = event.getUser();
        String message = "hi " + user;
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel().sendMessage(message).queue();
    }

    /**
     * 온라인으로 전환한 유저 리스너
     */
    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        Dotenv config = Dotenv.configure().load();
        String USER_ID = config.get("USER_ID");

        User user = event.getJDA().getUserById(USER_ID);

        List<Member> members = event.getGuild().getMembers();
        int onlineMember = 0;
        for (Member member : members){
            if(member.getOnlineStatus()== OnlineStatus.ONLINE){
                ++onlineMember;
            }
        }
        String userState = changeStateKr(event.getNewOnlineStatus().getKey());
        String message = " ** " + user.getName() + "** 님께서 "+userState+ "상태로 변경하셨습니다"+ "현재 온라인 유저는 "+onlineMember+"명 입니다";

        Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel().sendMessage(message).queue();
    }

    private String changeStateKr(String key) {
        switch (key) {
            case "offline":
                key= "오프라인";
                break;
            case "online":
                key= "온라인";
                break;
            case "idle":
                key= "자리비움";
                break;
            case "dnd":
                key= "방해금지";
                break;
            default:
                break;
        }
        return key;
    }
}
