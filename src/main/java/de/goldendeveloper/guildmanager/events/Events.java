package de.goldendeveloper.guildmanager.events;

import de.goldendeveloper.guildmanager.Main;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;

public class Events extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.settingsTName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.settingsTName);
                if (table.existsColumn(Main.colmGuild)) {
                    if (table.getColumn(Main.colmGuild).getAll().contains(e.getGuild().getId())) {
                        HashMap<String, Object> row = table.getRow(table.getColumn(Main.colmGuild), e.getGuild().getId());
                        if (!row.get(Main.colmWChannel).toString().isEmpty() || !row.get(Main.colmWChannel).toString().isBlank()) {
                            TextChannel ch = e.getGuild().getTextChannelById(row.get(Main.colmWChannel).toString());
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
                        } else if (!row.get(Main.colmJRole).toString().isEmpty() || !row.get(Main.colmJRole).toString().isBlank()) {
                            Role role = e.getGuild().getRoleById(row.get(Main.colmJRole).toString());
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
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.settingsTName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.settingsTName);
                if (table.existsColumn(Main.colmGuild)) {
                    HashMap<String, Object> row = table.getRow(table.getColumn(Main.colmGuild), event.getGuild().getId());
                    for (SelectOption option : event.getSelectedOptions()) {
                        if (option.getValue().equalsIgnoreCase(RegisterCommands.settingsSupJoinRole)) {
                            if (!row.get(Main.colmJRole).toString().isEmpty()) {
                                table.getColumn(Main.colmJRole).set("", Integer.parseInt(row.get("id").toString()));
                                event.getInteraction().reply("Die Einstellung für die Join Rolle wurde entfernt").queue();
                            } else {
                                event.getInteraction().reply("Es ist keine Einstellung mit dieser Option vorhanden!").queue();
                            }
                        } else if (option.getValue().equalsIgnoreCase(RegisterCommands.settingsSupWMessage)) {
                            if (!row.get(Main.colmWChannel).toString().isEmpty()) {
                                table.getColumn(Main.colmWChannel).set("", Integer.parseInt(row.get("id").toString()));
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
}
