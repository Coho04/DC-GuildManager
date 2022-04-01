package de.goldendeveloper.guildmanager.commands;

import de.goldendeveloper.guildmanager.ID;
import de.goldendeveloper.guildmanager.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ServerInvite {

    public ServerInvite(SlashCommandInteractionEvent e) {
        Guild g = e.getJDA().getGuildById(Main.getConfig().getDiscordServer());
        if (g != null) {
            e.getInteraction().reply("Mit dem Button unten kannst du unserem Server beitreten!")
                    .addActionRow(Button.link(g.getTextChannelById(747208323761176586L).createInvite().complete().getUrl(), "Zum Server"))
                    .queue();
        } else {
            e.getInteraction().reply(ID.hasError("Guild is NULL")).queue();
        }
    }
}
