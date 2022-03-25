package de.goldendeveloper.gdguildmanager.commands;

import de.goldendeveloper.gdguildmanager.ID;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CMD_Error_report {

    public static void onErrorReport(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            User c = e.getJDA().getUserById(ID._Coho04_MEMBER);
            if (c != null) {
                c.openPrivateChannel().queue(msg->{
                    MessageEmbed embed = new EmbedBuilder()
                            .setTitle(e.getUser().getName())
                            .setImage(e.getUser().getAvatarUrl())
                            .addField("ERROR", e.getOption("error").getAsString(), true)
                            .build();
                    msg.sendMessageEmbeds(embed).queue();
                });
                e.getInteraction().reply("Dein Report wurde erfolgreich abgesendet").queue();
            } else {
                e.getInteraction().reply(ID.hasError("_Coho04_ is NULL")).queue();
            }
        }
    }
}
