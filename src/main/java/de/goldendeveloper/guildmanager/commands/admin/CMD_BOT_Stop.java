package de.goldendeveloper.guildmanager.commands.admin;

import de.goldendeveloper.guildmanager.ID;
import de.goldendeveloper.guildmanager.Main;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CMD_BOT_Stop {

    public static void onBotStop(SlashCommandInteractionEvent e) {
        User u = e.getJDA().getUserById(ID._Coho04_MEMBER);
        if (u != null) {
            if (e.getUser() == u) {
                e.getInteraction().reply("Ich fahre mich nun herunter").queue();
                Main.getDiscord().getBot().shutdown();
            } else {
                e.getInteraction().reply(ID.hasError("Du musst der Bot Owner sein für diesen Command")).queue();
            }
        } else {
            e.getInteraction().reply(ID.hasError("User is NULL")).queue();
        }
    }
}
