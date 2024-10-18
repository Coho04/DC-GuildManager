package io.github.coho04.guildmanager.discord.commands;

import io.github.coho04.dcbcore.DCBot;
import io.github.coho04.dcbcore.interfaces.CommandInterface;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.concurrent.TimeUnit;

public class TimeOut implements CommandInterface {

    private final String optionUser = "user";
    private final String optionTime = "time";

    @Override
    public CommandData commandData() {
        return Commands.slash("time-out", "Schicke einen Benutzer in den Timeout")
                .addOption(OptionType.USER, optionUser, "Füge einen Benutzer hinzu", true)
                .addOption(OptionType.STRING, optionTime, "Gib die Timeout Dauer in Tagen an um den User zu timeouten. (In Minuten)", true);
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Member member = e.getMember();
        if (member == null) {
            e.reply("Ein fehler ist aufgetreten! Bitte versuche es später noch einmal!").queue();
            Sentry.captureMessage("CMD User ist NULL", SentryLevel.ERROR);
            return;
        }
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            Member targetMember = e.getOption(optionUser).getAsMember();
            long time = e.getOption(optionTime).getAsLong();
            if (targetMember == null) {
                e.reply("Ein fehler ist aufgetreten! Bitte versuche es später noch einmal!").queue();
                Sentry.captureMessage("User ist NULL", SentryLevel.ERROR);
                return;
            }
            if (time >= 0) {
                targetMember.timeoutFor(time, TimeUnit.MINUTES).queue();
                e.getInteraction().reply("Der User " + targetMember.getUser().getName() + " hat erfolgreich einen timeout bekommen!").queue();
            } else {
                e.reply("Ein Fehler ist aufgetreten! Bitte versuche es später noch einmal!").queue();
                Sentry.captureMessage("Timeout Dauer is 0", SentryLevel.ERROR);
            }
        } else {
            e.getInteraction().reply("[ERROR]: Für den Command hast du nicht genügend Rechte").queue();
        }
    }
}
