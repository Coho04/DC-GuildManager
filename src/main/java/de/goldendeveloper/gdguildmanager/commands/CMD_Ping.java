package de.goldendeveloper.gdguildmanager.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CMD_Ping {

    public static void onPing(SlashCommandEvent e) {
        long time = System.currentTimeMillis();
        e.getInteraction().reply("Pong!").queue(response -> response.sendMessage("Pong:" + System.currentTimeMillis() + time + " ms").queue());
    }
}
