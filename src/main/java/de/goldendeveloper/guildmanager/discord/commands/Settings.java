package de.goldendeveloper.guildmanager.discord.commands;

import de.goldendeveloper.dcbcore.DCBot;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import de.goldendeveloper.guildmanager.Main;
import de.goldendeveloper.guildmanager.MysqlConnection;
import de.goldendeveloper.mysql.entities.RowBuilder;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class Settings implements CommandInterface {

    private final String settingsSupJoinRole = "join-role";
    private final String settingsSupRemove = "remove";
    private final String settingsSupWMessage = "welcome-message";
    public final String settingsSupJoinRoleOptionRole = "role";
    public final String settingsSupWMessageOptionChannel = "channel";

    @Override
    public CommandData commandData() {
        return Commands.slash("settings", "Zeigt die Einstellungen des Servers an")
                .addSubcommands(
                        new SubcommandData(settingsSupJoinRole, "Die Rolle die einem User automatisch beim Joinen gegeben werden soll!").addOption(OptionType.ROLE, settingsSupJoinRoleOptionRole, "Join Rolle", true),
                        new SubcommandData(settingsSupRemove, "Zeigt die eine Liste aller Option die Entfernt werden können!"),
                        new SubcommandData(settingsSupWMessage, "Setzte einen Willkommens Channel um deine User willkommen zu heißen!").addOption(OptionType.CHANNEL, settingsSupWMessageOptionChannel, "Willkommens Channel", true)
                );
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (e.getSubcommandName().equalsIgnoreCase(settingsSupJoinRole)) {
                if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.dbName)) {
                    if (Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.settingsTName)) {
                        Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.settingsTName);
                        if (table.existsColumn(MysqlConnection.colmGuild)) {
                            Role role = e.getOption(settingsSupJoinRoleOptionRole).getAsRole();
                            if (table.getColumn(MysqlConnection.colmGuild).getAll().getAsString().contains(e.getGuild().getId())) {
                                table.getRow(table.getColumn(MysqlConnection.colmGuild), e.getGuild().getId()).set(table.getColumn(MysqlConnection.colmJRole), role.getId());
                                e.getInteraction().reply("Die Rolle fürs joinen wurde erfolgreich gesetzt!").queue();
                            } else {
                                table.insert(new RowBuilder()
                                        .with(table.getColumn(MysqlConnection.colmGuild), e.getGuild().getId())
                                        .with(table.getColumn(MysqlConnection.colmJRole), role.getId())
                                        .with(table.getColumn(MysqlConnection.colmWChannel), "")
                                        .build()
                                );
                                e.getInteraction().reply("Die Rolle fürs joinen wurde erfolgreich gesetzt!").queue();
                            }
                        }
                    }
                }
            } else if (e.getSubcommandName().equalsIgnoreCase(settingsSupWMessage)) {
                e.deferReply().queue();
                if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.dbName)) {
                    if (Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.settingsTName)) {
                        Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.settingsTName);
                        if (table.existsColumn(MysqlConnection.colmGuild)) {
                            TextChannel channel = e.getOption(settingsSupWMessageOptionChannel).getAsChannel().asTextChannel();
                            if (table.getColumn(MysqlConnection.colmGuild).getAll().getAsString().contains(e.getGuild().getId())) {
                                table.getRow(table.getColumn(MysqlConnection.colmGuild), e.getGuild().getId()).set(table.getColumn(MysqlConnection.colmWChannel), channel.getId());
                                e.getHook().sendMessage("Der Channel für die Willkommens Nachricht wurde erfolgreich gesetzt!").queue();
                            } else {
                                table.insert(new RowBuilder()
                                        .with(table.getColumn(MysqlConnection.colmGuild), e.getGuild().getId())
                                        .with(table.getColumn(MysqlConnection.colmJRole), "")
                                        .with(table.getColumn(MysqlConnection.colmWChannel), channel.getId())
                                        .build()
                                );
                                e.getHook().sendMessage("Der Channel für die Willkommens Nachricht wurde erfolgreich gesetzt!").queue();
                            }
                        }
                    }
                }
            } else if (e.getSubcommandName().equalsIgnoreCase(settingsSupRemove)) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**Remove Option**");
                builder.setDescription("Entferne hier eine eingestellte Option für diesen Discord Server");
                e.getInteraction().replyEmbeds(builder.build()).addActionRow(
                        StringSelectMenu.create("removeSelect")
                                .addOption("Join Role", settingsSupJoinRole)
                                .addOption("Willkommens Nachricht", settingsSupWMessage)
                                .build()
                ).queue();
            }
        } else {
            e.getInteraction().reply("[ERROR]: Für den Command hast du nicht genügend Rechte").queue();
        }
    }
}
