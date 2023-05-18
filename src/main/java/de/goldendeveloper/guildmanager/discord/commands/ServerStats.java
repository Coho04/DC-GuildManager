package de.goldendeveloper.guildmanager.discord.commands;

import de.goldendeveloper.dcbcore.DCBot;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import de.goldendeveloper.guildmanager.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.Color;

public class ServerStats implements CommandInterface {

    @Override
    public CommandData commandData() {
        return Commands.slash("server-stats", "Gibt die Serverstatistiken aus");
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("**Server Stats**");
        embedBuilder.setFooter("@Golden-Developer", e.getJDA().getSelfUser().getAvatarUrl());
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.addField("Alle Users", String.valueOf(e.getGuild().getMembers().size()), true);
        embedBuilder.addField("Online Users", String.valueOf(Main.getOnlineUsers(e.getGuild())), true);
        embedBuilder.addField("Offline Users", String.valueOf(Main.getOfflineUsers(e.getGuild())), true);
        embedBuilder.addField("AFK Users", String.valueOf(Main.getAfkUsers(e.getGuild())), true);
        embedBuilder.addField("Nicht Stören Users", String.valueOf(Main.getDoNotDisturbUsers(e.getGuild())), true);
        embedBuilder.addField("Channel", String.valueOf(e.getGuild().getChannels().size()), true);
        embedBuilder.addField("TextChannel", String.valueOf(e.getGuild().getTextChannels().size()), true);
        embedBuilder.addField("VoiceChannel", String.valueOf(e.getGuild().getVoiceChannels().size()), true);
        embedBuilder.addField("Server Owner", e.getGuild().getOwner().getUser().getName(), true);
        e.getInteraction().replyEmbeds(embedBuilder.build()).queue();
    }
}
