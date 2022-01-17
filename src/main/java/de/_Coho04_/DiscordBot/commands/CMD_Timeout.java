package de._Coho04_.DiscordBot.commands;

import de._Coho04_.DiscordBot.ID;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.concurrent.TimeUnit;

public class CMD_Timeout {

    public static void onTimeout(SlashCommandEvent e) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.ADMINISTRATOR)) {
                Member member = e.getOption("user").getAsMember();
                    long time = e.getOption("time").getAsLong();
                if (member != null) {
                    if (time >= 0) {
                        member.timeoutFor(time, TimeUnit.MINUTES).queue();
                        e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich getimeoutet!").queue();
                    } else {
                        e.getInteraction().reply(ID.hasError("Timeout Dauer is 0")).queue();
                    }
                } else {
                    e.getInteraction().reply(ID.hasError("User ist NULL")).queue();
                }
            } else {
                e.getInteraction().reply(ID.hasNoPermissions).queue();
            }
        } else {
            e.getInteraction().reply(ID.hasError("CMD User ist NULL")).queue();
        }
    }
}
