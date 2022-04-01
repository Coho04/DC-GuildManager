package de.goldendeveloper.guildmanager.commands;

import de.goldendeveloper.guildmanager.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class BotStats {

    public BotStats(SlashCommandInteractionEvent e) {
        Guild MainServer = e.getJDA().getGuildById(Main.getConfig().getDiscordServer());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Server Stats**");
        embed.setFooter("@Golden-Developer", e.getJDA().getSelfUser().getAvatarUrl());
        embed.setColor(Color.MAGENTA);
        embed.addField("Server", String.valueOf(e.getJDA().getGuilds().size()), true);
        embed.addField("Support-Server", MainServer.getName() , true);
        embed.addField("Bot-Owner", "@Golden-Developer", true);
        e.getInteraction().replyEmbeds(embed.build()).addActionRow(Button.link(MainServer.getDefaultChannel().createInvite().complete().getUrl(), MainServer.getName())).queue();
    }
}
