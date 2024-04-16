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
        Member member = e.getMember();
        if (member == null) {
            e.reply("Ein fehler ist aufgetreten! Bitte versuche es später noch einmal!").queue();
            Sentry.captureMessage("CMD User ist NULL", SentryLevel.ERROR);
            return;
        }
        if (member.hasPermission(Permission.KICK_MEMBERS) || member.hasPermission(Permission.ADMINISTRATOR)) {
            Member targetMember = e.getOption("user").getAsMember();
            String reason = "";
            if (e.getOption("reason") != null) {
                reason = e.getOption("reason").getAsString();
            }
            if (targetMember == null) {
                Sentry.captureMessage("User ist NULL", SentryLevel.ERROR);
                e.reply("Der User konnte nicht gefunden werden!").queue();
                return;
            }
            if (!reason.isEmpty()) {
                targetMember.kick().reason(reason).queue();
                e.getInteraction().reply("Der User " + targetMember.getUser().getName() + " wurde erfolgreich gekickt!").queue();
            } else {
                targetMember.kick().queue();
                e.getInteraction().reply("Der User " + targetMember.getUser().getName() + " wurde erfolgreich gekickt!").queue();
            }
        } else {
            e.getInteraction().reply("[ERROR]: Für den Command hast du nicht genügend Rechte").queue();
        }
    }
}