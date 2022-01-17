package de._Coho04_.DiscordBot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CMD_BOT_Join {

    public static void onBotJoin(SlashCommandEvent e) {
        e.getInteraction().reply("Hiermit kannst du mich auf deinen Server einladen: " + e.getJDA().setRequiredScopes("applications.commands").getInviteUrl(Permission.ADMINISTRATOR)).queue();
    }
}
