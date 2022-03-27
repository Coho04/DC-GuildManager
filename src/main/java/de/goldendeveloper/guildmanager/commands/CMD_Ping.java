package de.goldendeveloper.guildmanager.commands;


import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CMD_Ping {

    public static void onPing(SlashCommandInteractionEvent e) {
        long time = System.currentTimeMillis();
        e.getInteraction().reply("Pong!").queue(response -> response.sendMessage("Pong:" + System.currentTimeMillis() + time + " ms").queue());
    }
}
