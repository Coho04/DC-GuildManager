package de.goldendeveloper.gdguildmanager.commands;

import de.goldendeveloper.gdguildmanager.ID;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CMD_Birthday {

    public static void onBirthday(SlashCommandEvent e) {
        Member member = e.getMember();
        if (member != null) {
            User u = e.getOption("user").getAsUser();
            if (u != null) {
                if (e.getOption("private").getAsBoolean()) {
                    u.openPrivateChannel().queue(msg->{
                        msg.sendMessage("Herzlichen Glückwunsch zum Geburtstag wünscht dir " + member.getUser().getName() + ". Viel Spaß dir heute!!!").queue();
                    });
                } else {
                    e.getChannel().sendMessage(u.getAsMention() + "Herzlichen Glückwunsch zum Geburtstag wünscht dir " + member.getUser().getName() + ". Viel Spaß dir heute!!!").queue();
                }
                e.getInteraction().reply("Dem User wurde erfolgreich gratuliert!").queue();
            } else {
                e.getInteraction().reply(ID.hasError("B-User ist NULL")).queue();
            }
        } else {
            e.getInteraction().reply(ID.hasError("CMD Member ist NULL")).queue();
        }
    }
}
