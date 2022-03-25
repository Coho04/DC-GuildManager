package de.goldendeveloper.gdguildmanager.commands;

import de.goldendeveloper.gdguildmanager.ID;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CMD_BOT_Owner {

    public static void onBotOwner(SlashCommandInteractionEvent e) {
        User u = e.getJDA().getUserById(ID._Coho04_MEMBER);
        if (u != null) {
            e.getInteraction().reply("Der Bot Owner ist " + u.getAsMention()).queue();
        } else {
            e.getInteraction().reply("Der Bot Owner ist _Coho04_#6380").queue();
        }
    }
}
