package de.goldendeveloper.guildmanager.commands;

import de.goldendeveloper.guildmanager.ID;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GuildLeave {

    public GuildLeave(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.ADMINISTRATOR)) {
                e.getInteraction().reply("Ich werde nun Erfolgreich den Server verlassen!").queue();
                Guild g = e.getGuild();
                if (g != null) {
                    g.leave().queue();
                }
            } else {
                e.getInteraction().reply(ID.hasNoPermissions).queue();
            }
        } else {
            e.getInteraction().reply(ID.hasError("CMD User ist NULL")).queue();
        }
    }
}
