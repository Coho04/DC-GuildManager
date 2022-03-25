package de.goldendeveloper.gdguildmanager.commands;


import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CMD_Donate {

    public static void onDonate(SlashCommandInteractionEvent e)  {
        e.getInteraction().reply("Wenn du mir etwas Spenden möchtest dann kannst du dies gerne unter: https://spende.coho04.de/ machen! \n" + "Vielen Danke <3 !").queue();
    }
}
