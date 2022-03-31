package de.goldendeveloper.guildmanager.commands;

import de.goldendeveloper.guildmanager.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;

public class Help {

    public Help(SlashCommandInteractionEvent e) {
        List<Command> cmd = Main.getDiscord().getBot().retrieveCommands().complete();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Help Commands**");
        embed.setColor(Color.MAGENTA);
        embed.setFooter("@Golden-Developer", e.getJDA().getSelfUser().getAvatarUrl());
        for (Command c : cmd) {
            embed.addField("/" + c.getName(), c.getDescription(), true);
        }
        e.getInteraction().replyEmbeds(embed.build())
                .addActionRow(
                        Button.link("https://wiki.golden-developer.de", "Online Übersicht"),
                        Button.link("https://support.golden-developer.de", "Support Anfragen")
                ).queue();
    }
}
