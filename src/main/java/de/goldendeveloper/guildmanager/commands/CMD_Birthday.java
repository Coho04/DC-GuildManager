package de.goldendeveloper.guildmanager.commands;

import de.goldendeveloper.guildmanager.ID;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CMD_Birthday {

    public static void onBirthday(SlashCommandInteractionEvent e) {
        Member member = e.getMember();
        if (member != null) {
            User u = e.getOption("user").getAsUser();
            if (e.getOption("private").getAsBoolean()) {
                u.openPrivateChannel().queue(msg->{
                    msg.sendMessage("Herzlichen Glückwunsch zum Geburtstag wünscht dir " + member.getUser().getName() + ". Viel Spaß dir heute!!!").queue();
                });
            } else {
                e.getChannel().sendMessage(u.getAsMention() + "Herzlichen Glückwunsch zum Geburtstag wünscht dir " + member.getUser().getName() + ". Viel Spaß dir heute!!!").queue();
            }
            e.getInteraction().reply("Dem User wurde erfolgreich gratuliert!").queue();
        } else {
            e.getInteraction().reply(ID.hasError("CMD Member ist NULL")).queue();
        }
    }
}
