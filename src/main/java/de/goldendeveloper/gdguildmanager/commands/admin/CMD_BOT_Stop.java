package de.goldendeveloper.gdguildmanager.commands.admin;

import de.goldendeveloper.gdguildmanager.ID;
import de.goldendeveloper.gdguildmanager.Main;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CMD_BOT_Stop {

    public static void onBotStop(SlashCommandEvent e) {
        User u = e.getJDA().getUserById(ID._Coho04_MEMBER);
        if (u != null) {
            if (e.getUser() == u) {
                e.getInteraction().reply("Ich fahre mich nun herunter").queue();
                Main.Bot.shutdown();
            } else {
                e.getInteraction().reply(ID.hasError("Du musst der Bot Owner sein für diesen Command")).queue();
            }
        } else {
            e.getInteraction().reply(ID.hasError("User is NULL")).queue();
        }
    }
}
