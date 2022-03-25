package de.goldendeveloper.gdguildmanager.commands;

import de.goldendeveloper.gdguildmanager.ID;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;

public class CMD_BOT_Stats {

    public static void onBotStats(SlashCommandEvent e) {
        User user = e.getJDA().getUserById(ID._Coho04_MEMBER);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Server Stats**");
        embed.setFooter("@_Coho04_", e.getJDA().getSelfUser().getAvatarUrl());
        embed.setColor(Color.MAGENTA);

        Guild MainServer = e.getJDA().getGuildById(ID._Coho04_Community_Server);

        embed.addField("Server", String.valueOf(e.getJDA().getGuilds().size()), true);
        embed.addField("Main Server",MainServer.getName() , true);
        embed.addField("Bot Owner", user.getAsMention(), true);


        e.getInteraction().replyEmbeds(embed.build()).addActionRow(Button.link(MainServer.getDefaultChannel().createInvite().complete().getUrl(), MainServer.getName())).queue();
    }
}
