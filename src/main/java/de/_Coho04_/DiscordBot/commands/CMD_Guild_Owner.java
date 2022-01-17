package de._Coho04_.DiscordBot.commands;

import de._Coho04_.DiscordBot.ID;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CMD_Guild_Owner {

    public static void onGuildOwner(SlashCommandEvent e) {
        Guild g = e.getGuild();
        if (g != null) {
            if (g.getOwner() != null) {
                Member m = g.getOwner();
                if (m != null) {
                    User u = m.getUser();
                        if (!u.getName().isEmpty()) {
                            e.getInteraction().reply(e.getGuild().getOwner().getUser().getName()).queue();
                        } else {
                            e.getInteraction().reply(ID.hasError("Owner Name ist NULL")).queue();
                        }
                } else {
                    e.getInteraction().reply(ID.hasError("Owner Member ist NULL")).queue();
                }
            } else {
                e.getInteraction().reply(ID.hasError("Owner ist NULL")).queue();
            }
         } else {
            e.getInteraction().reply(ID.hasError("Guild ist NULL")).queue();
        }
    }
}
