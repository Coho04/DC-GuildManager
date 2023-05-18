package de.goldendeveloper.guildmanager.discord.events;

import de.goldendeveloper.guildmanager.MysqlConnection;
import de.goldendeveloper.guildmanager.Main;
import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.SearchResult;
import de.goldendeveloper.mysql.entities.Table;
import io.sentry.Sentry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Date;
import java.util.HashMap;

public class CustomEvents extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        MYSQL mysql = Main.getMysqlConnection().getMysql();
        try {
            if (mysql.existsDatabase(MysqlConnection.dbName) && mysql.getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.settingsTName)) {
                Table table = mysql.getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.settingsTName);
                if (table.existsColumn(MysqlConnection.colmGuild)) {
                    if (table.getColumn(MysqlConnection.colmGuild).getAll().getAsString().contains(e.getGuild().getId())) {
                        HashMap<String, SearchResult> row = table.getRow(table.getColumn(MysqlConnection.colmGuild), e.getGuild().getId()).getData();
                        if (!row.get(MysqlConnection.colmWChannel).getAsString().isEmpty() || !row.get(MysqlConnection.colmWChannel).getAsString().isBlank()) {
                            TextChannel textChannel = e.getGuild().getTextChannelById(row.get(MysqlConnection.colmWChannel).getAsString());
                            if (textChannel != null) {
                                User user = e.getMember().getUser();
                                String serverName = e.getGuild().getName();
                                EmbedBuilder emb = new EmbedBuilder();
                                emb.setColor(3447003);
                                emb.setTitle("**" + serverName + "**");
                                emb.setDescription("Willkommen **" + user.getName() + "**,\n" + " auf dem **" + serverName + "** Discord Server!");
                                emb.setTimestamp(new Date().toInstant());
                                emb.setThumbnail(user.getAvatarUrl());
                                emb.setFooter("@Golden-Developer");
                                textChannel.sendMessageEmbeds(emb.build()).queue();
                            } else {
                                Member owner = e.getGuild().getOwner();
                                if (owner != null) {
                                    owner.getUser().openPrivateChannel().queue(channel -> {
                                        EmbedBuilder em = new EmbedBuilder();
                                        em.setColor(Color.RED);
                                        em.setTitle("**ERROR**");
                                        em.setDescription("Der angegebene Willkommens Channel konnte auf **" + e.getGuild().getName() + "** nicht gefunden werden!");
                                        em.setTimestamp(new Date().toInstant());
                                        em.setFooter("@Golden-Developer");
                                        channel.sendMessageEmbeds(em.build()).queue();
                                    });
                                }
                            }
                        }
                        if (!row.get(MysqlConnection.colmJRole).getAsString().isEmpty() || !row.get(MysqlConnection.colmJRole).getAsString().isBlank()) {
                            Role role = e.getGuild().getRoleById(row.get(MysqlConnection.colmJRole).getAsString());
                            Member bot = e.getGuild().getMember(e.getJDA().getSelfUser());
                            if (role != null && bot != null && bot.canInteract(role)) {
                                e.getGuild().addRoleToMember(e.getMember(), role).queue();
                            } else {
                                Member owner = e.getGuild().getOwner();
                                if (owner != null) {
                                    owner.getUser().openPrivateChannel().queue(channel -> {
                                        EmbedBuilder em = new EmbedBuilder();
                                        em.setColor(Color.RED);
                                        em.setTitle("**ERROR**");
                                        em.setDescription("Die angegebene Join Rolle konnte auf **" + e.getGuild().getName() + "** nicht gefunden oder nicht mit ihr Interagiert werden werden!");
                                        em.setTimestamp(new Date().toInstant());
                                        em.setFooter("@Golden-Developer");
                                        channel.sendMessageEmbeds(em.build()).queue();
                                    });
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Sentry.captureException(exception);
        }
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        try {
            if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.dbName)) {
                if (Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.settingsTName)) {
                    Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.settingsTName);
                    if (table.existsColumn(MysqlConnection.colmGuild)) {
                        HashMap<String, SearchResult> row = table.getRow(table.getColumn(MysqlConnection.colmGuild), event.getGuild().getId()).getData();
                        for (SelectOption option : event.getSelectedOptions()) {
                            if (option.getValue().equalsIgnoreCase("join-role")) {
                                if (!row.get(MysqlConnection.colmJRole).getAsString().isEmpty()) {
                                    table.getRow(table.getColumn(MysqlConnection.colmGuild), event.getGuild().getId()).set(table.getColumn(MysqlConnection.colmJRole), "");
                                    event.getInteraction().reply("Die Einstellung für die Join Rolle wurde entfernt").queue();
                                } else {
                                    event.getInteraction().reply("Es ist keine Einstellung mit dieser Option vorhanden!").queue();
                                }
                            } else if (option.getValue().equalsIgnoreCase("welcome-message")) {
                                if (!row.get(MysqlConnection.colmWChannel).getAsString().isEmpty()) {
                                    table.getRow(table.getColumn(MysqlConnection.colmGuild), event.getGuild().getId()).set(table.getColumn(MysqlConnection.colmWChannel), "");
                                    event.getInteraction().reply("Die Einstellung für die Willkommens Nachricht wurde entfernt").queue();
                                } else {
                                    event.getInteraction().reply("Es ist keine Einstellung mit dieser Option vorhanden!").queue();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Sentry.captureException(exception);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent e) {
        try {
            String cmd = e.getName();
            if (cmd.equalsIgnoreCase("ban")) {
                if (e.getFocusedOption().getName().equalsIgnoreCase("time")) {
                    e.getInteraction().replyChoices(
                            new Command.Choice("Permanent", 9999),
                            new Command.Choice("14 Tag", 14),
                            new Command.Choice("7 Tag", 7),
                            new Command.Choice("5 Tag", 5),
                            new Command.Choice("3 Tag", 3),
                            new Command.Choice("2 Tag", 2),
                            new Command.Choice("1 Tag", 1)
                    ).queue();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Sentry.captureException(exception);
        }
    }
}
