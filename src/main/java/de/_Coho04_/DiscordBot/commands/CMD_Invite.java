package de._Coho04_.DiscordBot.commands;

import de._Coho04_.DiscordBot.ID;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CMD_Invite {

    public static void onInvite(SlashCommandEvent e) {
        Guild g = e.getJDA().getGuildById(ID._Coho04_Community_Server);
        if (g != null) {
            Invite inv = g.getTextChannelById(747208323761176586L).createInvite().complete();
            e.getInteraction().reply("Hiermit trittst du meinem Server bei:" + inv.getUrl()).queue();
        } else {
            e.getInteraction().reply(ID.hasError("Guild is NULL")).queue();
        }
    }
}
