package com.gahyeonbot;

import com.gahyeonbot.commands.*;
import com.gahyeonbot.listeners.CommandManager;
import com.gahyeonbot.listeners.EventListeners;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;


/**
 * 토큰을 env파일에서 획득
 * 표준샤드매니저빌더에 기본 봇에대한 정보를 주입한후 빌드
 * 1.createDefault : 봇의 식별자 (토큰) 지정
 * 2.setStatus : 봇의 상태 지정
 * 3.setActivity : 봇의 활동상태 지정
 * 4.enableIntents : 사용할 이벤트 권한지정 - 특정서버에서 메시지도배와 같은 무분별한 데이터를 받아 봇의 성능이 저하되는 상태를 방지 및
 * 개발자가 사용자의 메시지 무단 수집을 막기위해 추가됨.
 * 5.setMemberCachePolicy : 사용자 캐시 범위 지정 - 음성채널에 있는사람만 캐싱할지 등을 지정
 * 6.setChunkingFilter : 샤드 관리자가 서버의 멤버 및 채널 정보를 가져오는 방법을 설정하는 데 사용.
 * Discord의 API는 대규모 서버의 정보를 모두 한 번에 가져오는 것이 아니라 샤드(Chunk)단위로 정보를 획득.
 * ChunkingFilter.ALL은 모든 서버의 멤버 및 채널 정보를 가져옴. 이는 봇이 여러 개의 서버에 속한 멤버와 채널 정보를 한 번에 가져오는 데 사용
 * MemberCache,ChunkingFilte,CacheFlag 모두 많은 하드웨어 리소스를 사용하고 소모시킨다는점을 염두해둬야함
 */
public class Main {
    private final Dotenv config;
    private final ShardManager shardManager;

    public Main() {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder
                .createDefault(token)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("경재랑 데이트"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableCache(CacheFlag.ONLINE_STATUS);
        shardManager = builder.build();

        CommandManager manager = new CommandManager();
        manager.setShardManager(shardManager);
        manager.add(new Clean());
        manager.add(new Soloout());
        manager.add(new Outwith());
        shardManager.addEventListener(manager);
        EventListeners listeners = new EventListeners();
        shardManager.addEventListener(listeners);
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Dotenv getConfig() {
        return config;
    }

    public static void main(String[] args) throws LoginException {
        Main m = new Main();
    }
}