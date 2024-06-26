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

import java.util.Objects;

public class Clear implements CommandInterface {

    private final String optionAmount = "anzahl";

    @Override
    public CommandData commandData() {
        return Commands.slash("clear", "Löscht eine bestimmte Anzahl an Nachrichten")
                .addOption(OptionType.INTEGER, optionAmount, "Anzahl von löschenden Nachrichten!", true);
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Member member = e.getMember();
        if (member == null) {
            e.reply("Ein fehler ist aufgetreten! Bitte versuche es später noch einmal!").queue();
            Sentry.captureMessage("Member is NULL", SentryLevel.ERROR);
            return;
        }
        if (member.hasPermission(Permission.MESSAGE_MANAGE) || member.hasPermission(Permission.ADMINISTRATOR)) {
            e.getInteraction().reply("Nachrichten werden gelöscht!!!").queue();
            if (e.getOption(optionAmount) == null) {
                e.reply("Ein fehler ist aufgetreten! Bitte versuche es später noch einmal!").queue();
                Sentry.captureMessage("CMD User ist NULL", SentryLevel.ERROR);
                return;
            }
            long amount = Objects.requireNonNull(e.getOption(optionAmount)).getAsLong();
            if (amount >= 1) {
                e.getChannel().getIterableHistory().cache(false)
                        .stream()
                        .limit(amount)
                        .filter(message -> !message.isPinned())
                        .forEach(message -> e.getChannel().purgeMessages(message));
            } else {
                e.getInteraction().reply(member.getAsMention() + "\n " + "Bitte nutze eine höhere Zahl!").queue();
            }
        }
    }
}
