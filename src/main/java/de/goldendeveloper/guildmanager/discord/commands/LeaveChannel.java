package de.goldendeveloper.guildmanager.discord.commands;

import de.goldendeveloper.dcbcore.DCBot;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class LeaveChannel implements CommandInterface {

    @Override
    public CommandData commandData() {
        return Commands.slash("leave-channel", "Verlässt den aktuellen VoiceChannel");
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Guild guild = e.getGuild();
        if (e.getMember() != null && guild != null) {
            GuildVoiceState voiceState = guild.getSelfMember().getVoiceState();
            if (voiceState != null) {
                if (voiceState.getChannel() != null && voiceState.inAudioChannel()) {
                    e.getJDA().getDirectAudioController().disconnect(guild);
                    e.getInteraction().reply("Ich habe erfolgreich deinen Channel verlassen!").queue();
                } else {
                    e.getInteraction().reply(dcBot.getDiscord().hasError("Du bist in keinem VoiceChannel!")).queue();
                }
            } else {
                e.getInteraction().reply(dcBot.getDiscord().hasError("VoiceState is NULL")).queue();
            }
        } else {
            e.getInteraction().reply(dcBot.getDiscord().hasError("Benutzer is NULL")).queue();
        }
    }
}
