package de.goldendeveloper.guildmanager.commands;

import de.goldendeveloper.guildmanager.ID;
import de.goldendeveloper.guildmanager.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Invite {

    public Invite(SlashCommandInteractionEvent e) {
        Guild g = e.getJDA().getGuildById(Main.getConfig().getDiscordServer());
        if (g != null) {
            net.dv8tion.jda.api.entities.Invite inv = g.getTextChannelById(747208323761176586L).createInvite().complete();
            e.getInteraction().reply("Hiermit trittst du meinem Server bei:" + inv.getUrl()).queue();
        } else {
            e.getInteraction().reply(ID.hasError("Guild is NULL")).queue();
        }
    }
}
