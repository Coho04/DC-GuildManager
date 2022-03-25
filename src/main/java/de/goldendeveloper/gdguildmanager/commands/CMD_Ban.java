package de.goldendeveloper.gdguildmanager.commands;

import de.goldendeveloper.gdguildmanager.ID;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CMD_Ban {

    public static void onBanCommand(SlashCommandEvent e) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.BAN_MEMBERS) || m.hasPermission(Permission.ADMINISTRATOR)) {
                Member member = e.getOption("user").getAsMember();
                if (e.getOption("time") != null) {
                    Long time = e.getOption("time").getAsLong();
                    String reason = "";
                    if (e.getOption("reason") != null) {
                        reason = e.getOption("reason").getAsString();
                    }
                    if (member != null) {
                        if (!reason.isEmpty()) {
                            member.ban(time.intValue(), reason).queue();
                            e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gebannt!").queue();
                        } else {
                            member.ban(time.intValue()).queue();
                            e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gebannt!").queue();
                        }
                    } else {
                        e.getInteraction().reply(ID.hasError("User ist NULL")).queue();
                    }
                } else {
                    e.getInteraction().reply(ID.hasError("Zeit ist NULL")).queue();
                }
            } else {
                e.getInteraction().reply(ID.hasNoPermissions).queue();
            }
        } else {
            e.getInteraction().reply(ID.hasError("CMD User ist NULL")).queue();
        }
    }
}
