package de._Coho04_.DiscordBot.commands;

import de._Coho04_.DiscordBot.ID;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;

public class CMD_Server_Stats {

    public static void onServerStats(SlashCommandEvent e) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("**Server Stats**");
        embedBuilder.setFooter("@_Coho04_", e.getJDA().getSelfUser().getAvatarUrl());
        embedBuilder.setColor(Color.MAGENTA);

        embedBuilder.addField("Alle Users", String.valueOf(e.getGuild().getMembers().size()), true);
        embedBuilder.addField("Online Users", String.valueOf(ID.getOnlineUsers(e.getGuild())), true);
        embedBuilder.addField("Offline Users", String.valueOf(ID.getOfflineUsers(e.getGuild())), true);
        embedBuilder.addField("AFK Users", String.valueOf(ID.getAfkUsers(e.getGuild())), true);
        embedBuilder.addField("Nicht Stören Users", String.valueOf(ID.getDoNotDisturbUsers(e.getGuild())), true);

        embedBuilder.addField("Channel", String.valueOf(e.getGuild().getChannels().size()), true);
        embedBuilder.addField("TextChannel", String.valueOf(e.getGuild().getTextChannels().size()), true);
        embedBuilder.addField("VoiceChannel", String.valueOf(e.getGuild().getVoiceChannels().size()), true);
        embedBuilder.addField("Server Owner", e.getGuild().getOwner().getUser().getName(), true);

        e.getInteraction().replyEmbeds(embedBuilder.build()).queue();
    }
}
