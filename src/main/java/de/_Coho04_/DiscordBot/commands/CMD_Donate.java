package de._Coho04_.DiscordBot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CMD_Donate {

    public static void onDonate(SlashCommandEvent e)  {
        e.getInteraction().reply("Wenn du mir etwas Spenden möchtest dann kannst du dies gerne unter: https://spende.coho04.de/ machen! \n" + "Vielen Danke <3 !").queue();
    }
}
