package de.goldendeveloper.guildmanager.discord.commands;

import de.goldendeveloper.dcbcore.DCBot;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Birthday implements CommandInterface {

    @Override
    public CommandData commandData() {
        return Commands.slash("birthday", "Gratuliere einem anderen Benutzer")
                .addOption(OptionType.USER, "user", "Fügt einen anderen User hinzu", true)
                .addOption(OptionType.BOOLEAN, "private", "Möchtest du dem User die Glückwünsche privat zukommen lassen?");
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Member member = e.getMember();
        if (member != null) {
            User u = e.getOption("user").getAsUser();
            if (e.getOption("private").getAsBoolean()) {
                u.openPrivateChannel().queue(msg -> {
                    msg.sendMessage("Herzlichen Glückwunsch zum Geburtstag wünscht dir " + member.getUser().getName() + ". Viel Spaß dir heute!!!").queue();
                });
            } else {
                e.getChannel().sendMessage(u.getAsMention() + "Herzlichen Glückwunsch zum Geburtstag wünscht dir " + member.getUser().getName() + ". Viel Spaß dir heute!!!").queue();
            }
            e.getInteraction().reply("Dem User wurde erfolgreich gratuliert!").queue();
        } else {
            e.getInteraction().reply(dcBot.getDiscord().hasError("CMD Member ist NULL")).queue();
        }
    }
}
