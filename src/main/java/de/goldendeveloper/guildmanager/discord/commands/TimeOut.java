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
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.ADMINISTRATOR)) {
                Member member = e.getOption(optionUser).getAsMember();
                long time = e.getOption(optionTime).getAsLong();
                if (member != null) {
                    if (time >= 0) {
                        member.timeoutFor(time, TimeUnit.MINUTES).queue();
                        e.getInteraction().reply("Der User " + member.getUser().getName() + " hat erfolgreich einen timeout bekommen!").queue();
                    } else {
                        Sentry.captureMessage("Timeout Dauer is 0", SentryLevel.ERROR);
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
