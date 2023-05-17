package de.goldendeveloper.guildmanager.discord.commands;

import de.goldendeveloper.dcbcore.DCBot;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ServerOwner implements CommandInterface {


    @Override
    public CommandData commandData() {
        return Commands.slash("server-owner", "Gibt den Server Owner aus");
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Guild guild = e.getGuild();
        if (guild != null) {
            if (guild.getOwner() != null) {
                User user = guild.getOwner().getUser();
                if (!user.getName().isEmpty()) {
                    e.getInteraction().reply("Der Serverinhaber ist: " + e.getGuild().getOwner().getUser().getName()).queue();
                } else {
                    e.getInteraction().reply(dcBot.getDiscord().hasError("Owner Name ist NULL")).queue();
                }
            } else {
                e.getInteraction().reply(dcBot.getDiscord().hasError("Owner ist NULL")).queue();
            }
        } else {
            e.getInteraction().reply(dcBot.getDiscord().hasError("Guild ist NULL")).queue();
        }
    }
}
