package de._Coho04_.DiscordBot.commands;

import de._Coho04_.DiscordBot.ID;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CMD_Kick {

    public static void onKickCommand(SlashCommandEvent e) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.KICK_MEMBERS) || m.hasPermission(Permission.ADMINISTRATOR)) {
                Member member = e.getOption("user").getAsMember();
                    String reason = "";
                    if (e.getOption("reason") != null) {
                        reason = e.getOption("reason").getAsString();
                    }
                    if (member != null) {
                        if (!reason.isEmpty()) {
                            member.kick(reason).queue();
                            e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gekickt!").queue();
                        } else {
                            member.kick().queue();
                            e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gekickt!").queue();
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
