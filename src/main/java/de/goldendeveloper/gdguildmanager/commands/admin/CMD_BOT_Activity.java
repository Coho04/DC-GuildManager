package de.goldendeveloper.gdguildmanager.commands.admin;

import de.goldendeveloper.gdguildmanager.ID;
import de.goldendeveloper.gdguildmanager.Main;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CMD_BOT_Activity {

    public static void onActivity(SlashCommandInteractionEvent e) {
        User u = e.getJDA().getUserById(ID._Coho04_MEMBER);
        String acv = e.getOption("activity").getAsString();
        String url = e.getOption("url").getAsString();
        String type = e.getOption("type").getAsString();
        if (u != null) {
            if (e.getUser() == u) {
                if (!acv.isEmpty()) {
                    if (Main.getDiscord().getBot().getShardManager() != null) {
                        if (type.equalsIgnoreCase("playing")) {
                            Main.getDiscord().getBot().getShardManager().setActivity(Activity.playing(acv));
                        } else if (type.equalsIgnoreCase("watching")) {
                            Main.getDiscord().getBot().getShardManager().setActivity(Activity.watching(acv));
                        } else if (type.equalsIgnoreCase("competing")) {
                            Main.getDiscord().getBot().getShardManager().setActivity(Activity.competing(acv));
                        } else if (type.equalsIgnoreCase("streaming")) {
                            if (!url.isEmpty()) {
                                Main.getDiscord().getBot().getShardManager().setActivity(Activity.streaming(acv, url));
                            } else {
                                e.getInteraction().reply(ID.hasError("URL is Empty")).queue();
                            }
                        } else if (type.equalsIgnoreCase("listening")) {
                            Main.getDiscord().getBot().getShardManager().setActivity(Activity.listening(acv));
                        } else if (type.equalsIgnoreCase("custom")) {
                            if (!url.isEmpty()) {
                                Main.getDiscord().getBot().getShardManager().setActivity(Activity.of(Activity.ActivityType.CUSTOM_STATUS, acv, url));
                            } else {
                                e.getInteraction().reply(ID.hasError("URL is Empty")).queue();
                            }
                        }
                    } else {
                        e.getInteraction().reply(ID.hasError("ShardManager is NULL")).queue();
                    }
                } else {
                    e.getInteraction().reply(ID.hasError("Activity is Empty")).queue();
                }
            } else {
                e.getInteraction().reply(ID.hasError("Du musst der Bot Owner sein für diesen Command")).queue();
            }
        } else {
            e.getInteraction().reply(ID.hasError("User is NULL")).queue();
        }
    }
}
