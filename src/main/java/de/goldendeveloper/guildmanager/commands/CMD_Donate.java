package de.goldendeveloper.guildmanager.commands;


import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CMD_Donate {

    public static void onDonate(SlashCommandInteractionEvent e)  {
        e.getInteraction().reply("Wenn du uns etwas Spenden möchtest dann kannst du dies gerne unter: https://spende.golden-developer.de/ machen! \n" + "Vielen Danke <3 !").queue();
    }
}
