package de.goldendeveloper.guildmanager.events;

import de.goldendeveloper.guildmanager.CreateMysql;
import de.goldendeveloper.guildmanager.Main;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;

public class Events extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        if (Main.getCreateMysql().getMysql().existsDatabase(CreateMysql.dbName)) {
            if (Main.getCreateMysql().getMysql().getDatabase(CreateMysql.dbName).existsTable(CreateMysql.settingsTName)) {
                Table table = Main.getCreateMysql().getMysql().getDatabase(CreateMysql.dbName).getTable(CreateMysql.settingsTName);
                if (table.existsColumn(CreateMysql.colmGuild)) {
                    if (table.getColumn(CreateMysql.colmGuild).getAll().contains(e.getGuild().getId())) {
                        HashMap<String, Object> row = table.getRow(table.getColumn(CreateMysql.colmGuild), e.getGuild().getId()).get();
                        if (!row.get(CreateMysql.colmWChannel).toString().isEmpty() || !row.get(CreateMysql.colmWChannel).toString().isBlank()) {
                            TextChannel ch = e.getGuild().getTextChannelById(row.get(CreateMysql.colmWChannel).toString());
                            if (ch != null) {
                                User user = e.getMember().getUser();
                                String ServerName = e.getGuild().getName();
                                EmbedBuilder emb = new EmbedBuilder();
                                emb.setColor(3447003);
                                emb.setTitle("**" + ServerName + "**");
                                emb.setDescription("Willkommen **" + user.getName() + "**,\n" +
                                        " auf dem **" + ServerName + "** Discord Server!");
                                emb.setTimestamp(Main.getDiscord().getDate());
                                emb.setThumbnail(user.getAvatarUrl());
                                emb.setFooter("@Golden-Developer");
                                ch.sendMessageEmbeds(emb.build()).queue();
                            } else {
                                Member Owner = e.getGuild().getOwner();
                                if (Owner != null) {
                                    Owner.getUser().openPrivateChannel().queue(channel -> {
                                        EmbedBuilder em = new EmbedBuilder();
                                        em.setColor(Color.RED);
                                        em.setTitle("**ERROR**");
                                        em.setDescription("Der angegebene Willkommens Channel konnte auf **" + e.getGuild().getName() + "** nicht gefunden werden!");
                                        em.setTimestamp(Main.getDiscord().getDate());
                                        em.setFooter("@Golden-Developer");
                                        channel.sendMessageEmbeds(em.build()).queue();
                                    });
                                }
                            }
                        } else if (!row.get(CreateMysql.colmJRole).toString().isEmpty() || !row.get(CreateMysql.colmJRole).toString().isBlank()) {
                            Role role = e.getGuild().getRoleById(row.get(CreateMysql.colmJRole).toString());
                            Member bot = e.getGuild().getMember(e.getJDA().getSelfUser());
                            if (role != null) {
                                if (bot != null && bot.canInteract(role)) {
                                    e.getGuild().addRoleToMember(e.getMember(), role).queue();
                                } else {
                                    Member Owner = e.getGuild().getOwner();
                                    if (Owner != null) {
                                        Owner.getUser().openPrivateChannel().queue(channel -> {
                                            EmbedBuilder em = new EmbedBuilder();
                                            em.setColor(Color.RED);
                                            em.setTitle("**ERROR**");
                                            em.setDescription("Mit der angegebene Join Rolle konnte auf **" + e.getGuild().getName() + "** nicht interagiert werden!");
                                            em.setTimestamp(Main.getDiscord().getDate());
                                            em.setFooter("@Golden-Developer");
                                            channel.sendMessageEmbeds(em.build()).queue();
                                        });
                                    }
                                }
                            } else {
                                Member Owner = e.getGuild().getOwner();
                                if (Owner != null) {
                                    Owner.getUser().openPrivateChannel().queue(channel -> {
                                        EmbedBuilder em = new EmbedBuilder();
                                        em.setColor(Color.RED);
                                        em.setTitle("**ERROR**");
                                        em.setDescription("Die angegebene Join Rolle konnte auf **" + e.getGuild().getName() + "** nicht gefunden werden!");
                                        em.setTimestamp(Main.getDiscord().getDate());
                                        em.setFooter("@Golden-Developer");
                                        channel.sendMessageEmbeds(em.build()).queue();
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        if (Main.getCreateMysql().getMysql().existsDatabase(CreateMysql.dbName)) {
            if (Main.getCreateMysql().getMysql().getDatabase(CreateMysql.dbName).existsTable(CreateMysql.settingsTName)) {
                Table table = Main.getCreateMysql().getMysql().getDatabase(CreateMysql.dbName).getTable(CreateMysql.settingsTName);
                if (table.existsColumn(CreateMysql.colmGuild)) {
                    HashMap<String, Object> row = table.getRow(table.getColumn(CreateMysql.colmGuild), event.getGuild().getId()).get();
                    for (SelectOption option : event.getSelectedOptions()) {
                        if (option.getValue().equalsIgnoreCase(RegisterCommands.settingsSupJoinRole)) {
                            if (!row.get(CreateMysql.colmJRole).toString().isEmpty()) {
                                table.getRow(table.getColumn(CreateMysql.colmGuild), event.getGuild().getId()).set(table.getColumn(CreateMysql.colmJRole), "");
                                event.getInteraction().reply("Die Einstellung für die Join Rolle wurde entfernt").queue();
                            } else {
                                event.getInteraction().reply("Es ist keine Einstellung mit dieser Option vorhanden!").queue();
                            }
                        } else if (option.getValue().equalsIgnoreCase(RegisterCommands.settingsSupWMessage)) {
                            if (!row.get(CreateMysql.colmWChannel).toString().isEmpty()) {
                                table.getRow(table.getColumn(CreateMysql.colmGuild), event.getGuild().getId()).set(table.getColumn(CreateMysql.colmWChannel), "");
                                event.getInteraction().reply("Die Einstellung für die Willkommens Nachricht wurde entfernt").queue();
                            } else {
                                event.getInteraction().reply("Es ist keine Einstellung mit dieser Option vorhanden!").queue();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent e) {
        System.exit(0);
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent e) {
        String cmd = e.getName();
        if (cmd.equalsIgnoreCase(RegisterCommands.Ban)) {
            System.out.println(e.getFocusedOption().getName());
            if (e.getFocusedOption().getName().equalsIgnoreCase(RegisterCommands.BanOptionTime)) {
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
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent e) {
        System.out.println(e.getName());
    }
}
