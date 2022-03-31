package de.goldendeveloper.guildmanager.commands.moderate;

import de.goldendeveloper.guildmanager.ID;
import de.goldendeveloper.guildmanager.Main;
import de.goldendeveloper.guildmanager.events.RegisterCommands;
import de.goldendeveloper.mysql.entities.Row;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;

public class Settings {

    public Settings(SlashCommandInteractionEvent e) {
        if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (e.getSubcommandName().equalsIgnoreCase(RegisterCommands.settingsSupJoinRole)) {
                if (Main.getMysql().existsDatabase(Main.dbName)) {
                    if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.settingsTName)) {
                        Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.settingsTName);
                        if (table.existsColumn(Main.colmGuild)) {
                            Role role = e.getOption(RegisterCommands.settingsSupJoinRoleOptionRole).getAsRole();
                            if (table.getColumn(Main.colmGuild).getAll().contains(e.getGuild().getId())) {
                                HashMap<String, Object> row = table.getRow(table.getColumn(Main.colmGuild), e.getGuild().getId());
                                table.getColumn(Main.colmJRole).set(role.getId(), Integer.parseInt(row.get("id").toString()));
                                e.getInteraction().reply("Die Rolle fürs joinen wurde erfolgreich gesetzt!").queue();
                            } else {
                                table.insert(new Row(table, table.getDatabase())
                                        .with(Main.colmGuild, e.getGuild().getId())
                                        .with(Main.colmJRole, role.getId())
                                        .with(Main.colmWChannel, "")
                                );
                                e.getInteraction().reply("Die Rolle fürs joinen wurde erfolgreich gesetzt!").queue();
                            }
                        } else {
                            Main.getDiscord().sendErrorMessage("Column " + Main.colmGuild + " in " + Main.settingsTName + " existiert nicht");
                        }
                    } else {
                        Main.getDiscord().sendErrorMessage("Table " + Main.settingsTName + " in " + Main.dbName + " existiert nicht");
                    }
                } else {
                    Main.getDiscord().sendErrorMessage("Database " + Main.dbName + " existiert nicht");
                }
            } else if (e.getSubcommandName().equalsIgnoreCase(RegisterCommands.settingsSupWMessage)) {
                if (Main.getMysql().existsDatabase(Main.dbName)) {
                    if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.settingsTName)) {
                        Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.settingsTName);
                        if (table.existsColumn(Main.colmGuild)) {
                            TextChannel channel = e.getOption(RegisterCommands.settingsSupWMessageOptionChannel).getAsTextChannel();
                            if (channel != null) {
                                if (table.getColumn(Main.colmGuild).getAll().contains(e.getGuild().getId())) {
                                    HashMap<String, Object> row = table.getRow(table.getColumn(Main.colmGuild), e.getGuild().getId());
                                    table.getColumn(Main.colmWChannel).set(channel.getId(), Integer.parseInt(row.get("id").toString()));
                                    e.getInteraction().reply("Der Channel für die Willkommens Nachricht wurde erfolgreich gesetzt!").queue();
                                } else {
                                    table.insert(new Row(table, table.getDatabase())
                                            .with(Main.colmGuild, e.getGuild().getId())
                                            .with(Main.colmJRole, "")
                                            .with(Main.colmWChannel, channel.getId())
                                    );
                                    e.getInteraction().reply("Der Channel für die Willkommens Nachricht wurde erfolgreich gesetzt!").queue();
                                }
                            } else {
                                e.getInteraction().reply("Der Angegebene Channel konnte nicht gefunden werden!").queue();
                            }
                        } else {
                            Main.getDiscord().sendErrorMessage("Column " + Main.colmGuild + " in " + Main.settingsTName + " existiert nicht");
                        }
                    } else {
                        Main.getDiscord().sendErrorMessage("Table " + Main.settingsTName + " in " + Main.dbName + " existiert nicht");
                    }
                } else {
                    Main.getDiscord().sendErrorMessage("Database " + Main.dbName + " existiert nicht");
                }
            } else if (e.getSubcommandName().equalsIgnoreCase(RegisterCommands.settingsSupRemove)) {
                if (Main.getMysql().existsDatabase(Main.dbName)) {
                    if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.settingsTName)) {
                        Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.settingsTName);
                        if (table.existsColumn(Main.colmGuild)) {
                            HashMap<String, Object> row = table.getRow(table.getColumn(Main.colmGuild), e.getGuild().getId());
                            String removeOption = e.getOption("").getAsString();
                            switch (removeOption) {
                                case RegisterCommands.settingsSupJoinRole -> {
                                    if (!row.get(Main.colmJRole).toString().isEmpty()) {
                                        table.getColumn(Main.colmJRole).set("", Integer.parseInt(row.get("id").toString()));
                                        e.getInteraction().reply("Die Einstellung für die Join Rolle wurde entfernt").queue();
                                    } else {
                                        e.getInteraction().reply("Es ist keine Einstellung mit dieser Option vorhanden!").queue();
                                    }
                                }
                                case RegisterCommands.settingsSupWMessage -> {
                                    if (!row.get(Main.colmWChannel).toString().isEmpty()) {
                                        table.getColumn(Main.colmWChannel).set("", Integer.parseInt(row.get("id").toString()));
                                        e.getInteraction().reply("Die Einstellung für die Willkommens Nachricht wurde entfernt").queue();
                                    } else {
                                        e.getInteraction().reply("Es ist keine Einstellung mit dieser Option vorhanden!").queue();
                                    }
                                }
                            }
                        } else {
                            Main.getDiscord().sendErrorMessage("Column " + Main.colmGuild + " in " + Main.settingsTName + " existiert nicht");
                        }
                    } else {
                        Main.getDiscord().sendErrorMessage("Table " + Main.settingsTName + " in " + Main.dbName + " existiert nicht");
                    }
                } else {
                    Main.getDiscord().sendErrorMessage("Database " + Main.dbName + " existiert nicht");
                }
            }
        } else {
            e.getInteraction().reply(ID.hasNoPermissions).queue();
        }
    }
}
