package de.goldendeveloper.guildmanager.commands.moderate;

import de.goldendeveloper.guildmanager.ID;
import de.goldendeveloper.guildmanager.events.RegisterCommands;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.List;

public class Clear {

    public Clear(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.MESSAGE_MANAGE) || m.hasPermission(Permission.ADMINISTRATOR)) {
                e.getInteraction().reply("Nachrichten gelöscht!!!").queue();
                if (e.getOption(RegisterCommands.ClearOptionAmount) != null) {
                    long amount = e.getOption(RegisterCommands.ClearOptionAmount).getAsLong();
                    if (amount >= 1) {
                        List<Message> messages = new ArrayList<>();
                        long i = amount + 1;
                        for (Message message : e.getChannel().getIterableHistory().cache(false)) {
                            if (!message.isPinned()) {
                                messages.add(message);
                            }
                            if (--i <= 0) break;
                        }
                        e.getChannel().purgeMessages(messages);
                    } else {
                        e.getInteraction().reply(m.getAsMention() + "\n " + "Bitte nutze eine höhere Zahl!").queue();
                    }
                }
            } else {
                e.getInteraction().reply(ID.hasError("CMD User ist NULL")).queue();
            }
        }
    }
}
