package de.goldendeveloper.guildmanager.discord.commands;

import de.goldendeveloper.dcbcore.DCBot;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.concurrent.TimeUnit;

public class Ban implements CommandInterface {

    private static final String optionUser = "user";
    private static final String optionReason = "reason";
    private static final String optionTime = "time";

    @Override
    public CommandData commandData() {
        return Commands.slash("ban",  "Bannt einen definierten Benutzer").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.INTEGER, "time", "Gib die Ban Dauer in Tagen an um den User zu bannen", true, true).addOption(OptionType.STRING, "reason", "Begründe deinen Ban", true);
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.BAN_MEMBERS) || m.hasPermission(Permission.ADMINISTRATOR)) {
                Member member = e.getOption(optionUser).getAsMember();
                if (e.getOption(optionTime) != null) {
                    int time = e.getOption(optionTime).getAsInt();
                    String reason = "";
                    if (e.getOption(optionReason) != null) {
                        reason = e.getOption(optionReason).getAsString();
                    }
                    if (member != null) {
                        if (!reason.isEmpty()) {
                            member.ban(time, TimeUnit.MINUTES).reason(reason).queue();
                            e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gebannt!").queue();
                        } else {
                            member.ban(time, TimeUnit.MINUTES).queue();
                            e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gebannt!").queue();
                        }
                    } else {
                        e.getInteraction().reply(dcBot.getDiscord().hasError("User ist NULL")).queue();
                    }
                } else {
                    e.getInteraction().reply(dcBot.getDiscord().hasError("Zeit ist NULL")).queue();
                }
            } else {
                e.getInteraction().reply("[ERROR]: Für den Command hast du nicht genügend Rechte").queue();
            }
        } else {
            e.getInteraction().reply(dcBot.getDiscord().hasError("CMD User ist NULL")).queue();
        }
    }
}
