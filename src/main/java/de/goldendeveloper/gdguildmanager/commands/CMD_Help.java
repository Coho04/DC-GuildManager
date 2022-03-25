package de.goldendeveloper.gdguildmanager.commands;

import de.goldendeveloper.gdguildmanager.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.List;

public class CMD_Help {

    public static void onHelp(SlashCommandEvent e) {
       List<Command> cmds =  Main.Bot.retrieveCommands().complete();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Help Commands**");
        embed.setColor(Color.MAGENTA);
        embed.setFooter("@_Coho04_#6380", e.getJDA().getSelfUser().getAvatarUrl());
        for (Command cmd : cmds) {
            embed.addField("/" + cmd.getName(), cmd.getDescription(), true);
        }
        e.getInteraction().replyEmbeds(embed.build()).addActionRow(Button.link("https://wiki.coho04.de/bots/discord/", "Online Übersicht"))
                .addActionRow(Button.link("https://support.coho04.de", "Support Anfragen")).queue();
    }
}
