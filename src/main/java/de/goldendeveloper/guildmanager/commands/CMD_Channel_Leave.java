package de.goldendeveloper.guildmanager.commands;

import de.goldendeveloper.guildmanager.ID;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CMD_Channel_Leave {

    public static void onLeaveChannel(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            Guild g = e.getGuild();
            if (g != null) {
                GuildVoiceState voice = g.getSelfMember().getVoiceState();
                if (voice != null) {
                    if (voice.getChannel() != null) {
                        if (voice.inAudioChannel()) {
                            e.getJDA().getDirectAudioController().disconnect(g);
                            e.getInteraction().reply("Ich habe erfolgreich deinen Channel verlassen!").queue();
                        } else {
                            e.getInteraction().reply(ID.hasError("Du bist in keinem VoiceChannel!")).queue();
                        }
                    } else {
                        e.getInteraction().reply(ID.hasError("Voice Channel is NULL")).queue();
                    }
                } else {
                    e.getInteraction().reply(ID.hasError("VoiceState is NULL")).queue();
                }
            } else {
                e.getInteraction().reply(ID.hasError("Guild is NULL")).queue();
            }
        } else {
            e.getInteraction().reply(ID.hasError("MEMBER is NULL")).queue();
        }
    }
}
