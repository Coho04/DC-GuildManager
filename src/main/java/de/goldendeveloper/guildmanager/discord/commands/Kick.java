package de.goldendeveloper.guildmanager.discord.commands;

import de.goldendeveloper.dcbcore.DCBot;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Kick implements CommandInterface {


    @Override
    public CommandData commandData() {
        return Commands.slash("kick", "Kickt einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.STRING, "reason", "Begründe deinen Kick");
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.KICK_MEMBERS) || m.hasPermission(Permission.ADMINISTRATOR)) {
                Member member = e.getOption("user").getAsMember();
                String reason = "";
                if (e.getOption("reason") != null) {
                    reason = e.getOption("reason").getAsString();
                }
                if (member != null) {
                    if (!reason.isEmpty()) {
                        member.kick().reason(reason).queue();
                        e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gekickt!").queue();
                    } else {
                        member.kick().queue();
                        e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gekickt!").queue();
                    }
                } else {
                    Sentry.captureMessage("User ist NULL", SentryLevel.ERROR);
                }
            } else {
                e.getInteraction().reply("[ERROR]: Für den Command hast du nicht genügend Rechte").queue();
            }
        } else {
            Sentry.captureMessage("CMD User ist NULL", SentryLevel.ERROR);
        }
    }
}
