package com.gahyeonbot.listeners;

import com.gahyeonbot.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private final List<ICommand> commands = new ArrayList<>();

    //전역 커멘드 : 한계없음. 업데이트까지 시간이걸림
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (ICommand command : commands) {
                guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
            }
        }
    }

    //길드 커멘드 : 최대 100개까지 제한
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (ICommand command : commands) {
                //guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
            }
        }
    }


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (ICommand command : commands) {
            if (command.getName().equals(event.getName())){
                command.execute(event);
                return;
            }
        }
    }

    public void add(ICommand command){
        commands.add(command);
    }
}