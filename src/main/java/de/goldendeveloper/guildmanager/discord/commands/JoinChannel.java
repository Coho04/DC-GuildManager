package de.goldendeveloper.guildmanager.discord.commands;

import de.goldendeveloper.dcbcore.DCBot;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class JoinChannel implements CommandInterface  {

    @Override
    public CommandData commandData() {
        return Commands.slash("join-channel", "Join the current VoiceChannel");
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Member member = e.getMember();
        if (member != null) {
            GuildVoiceState voice = member.getVoiceState();
            if (voice != null) {
                if (voice.getChannel() != null) {
                    if (voice.inAudioChannel()) {
                        e.getJDA().getDirectAudioController().connect(voice.getChannel());
                        e.getInteraction().reply("Ich habe erfolgreich deinen Channel betreten!").queue();
                    } else {
                        e.getInteraction().reply(dcBot.getDiscord().hasError("Du bist in keinem VoiceChannel!")).queue();
                    }
                } else {
                    e.getInteraction().reply(dcBot.getDiscord().hasError("Voice Channel is NULL")).queue();
                }
            } else {
                e.getInteraction().reply(dcBot.getDiscord().hasError("VoiceState is NULL")).queue();
            }
        } else {
            e.getInteraction().reply(dcBot.getDiscord().hasError("Member is NULL")).queue();
        }
    }
}
