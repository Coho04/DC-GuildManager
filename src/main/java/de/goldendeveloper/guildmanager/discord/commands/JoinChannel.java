package de.goldendeveloper.guildmanager.discord.commands;

import io.github.coho04.dcbcore.DCBot;
import io.github.coho04.dcbcore.interfaces.CommandInterface;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class JoinChannel implements CommandInterface {

    @Override
    public CommandData commandData() {
        return Commands.slash("join-channel", "Join the current VoiceChannel");
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Member member = e.getMember();
        if (member == null) {
            Sentry.captureMessage("Member is NULL", SentryLevel.ERROR);
            e.reply("Ein fehler ist aufgetreten! Bitte versuche es später noch einmal!").queue();
            return;
        }

        GuildVoiceState voice = member.getVoiceState();
        if (voice == null) {
            Sentry.captureMessage("VoiceState is NULL", SentryLevel.ERROR);
            e.reply("Ein fehler ist aufgetreten! Bitte versuche es später noch einmal!").queue();
            return;
        }

        if (voice.getChannel() == null) {
            Sentry.captureMessage("Voice Channel is NULL", SentryLevel.ERROR);
            e.reply("Ein fehler ist aufgetreten! Bitte versuche es später noch einmal!").queue();
            return;
        }

        if (voice.inAudioChannel()) {
            e.getJDA().getDirectAudioController().connect(voice.getChannel());
            e.getInteraction().reply("Ich habe erfolgreich deinen Channel betreten!").queue();
        } else {
            e.reply("Du bist in keinem VoiceChannel!").queue();
        }
    }
}
