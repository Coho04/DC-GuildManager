package de._Coho04_.DiscordBot.commands;

import de._Coho04_.DiscordBot.ID;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class CMD_Channel_Join {

    public static void onJoinChannel(SlashCommandEvent e) {
        Member m = e.getMember();
        if (m != null) {
            GuildVoiceState voice = m.getVoiceState();
            if (voice != null) {
                if (voice.getChannel() != null) {
                    if (voice.inAudioChannel()) {
                        e.getJDA().getDirectAudioController().connect(voice.getChannel());
                        e.getInteraction().reply("Ich habe erfolgreich deinen Channel betreten!").queue();
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
            e.getInteraction().reply(ID.hasError("Member is NULL")).queue();
        }
    }
}
