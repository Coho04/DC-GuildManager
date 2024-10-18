package io.github.coho04.guildmanager.discord.commands;

import io.github.coho04.guildmanager.Main;
import io.github.coho04.dcbcore.DCBot;
import io.github.coho04.dcbcore.interfaces.CommandInterface;
import io.sentry.Sentry;
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

import java.sql.*;
import java.util.Objects;

public class Settings implements CommandInterface {

    private final String settingsSupJoinRole = "join-role";
    private final String settingsSupRemove = "remove";
    private final String settingsSupWMessage = "welcome-message";
    public final String settingsSupJoinRoleOptionRole = "role";
    public final String settingsSupWMessageOptionChannel = "channel";

    @Override
    public CommandData commandData() {
        return Commands.slash("settings", "Zeigt die Einstellungen des Servers an").addSubcommands(new SubcommandData(settingsSupJoinRole, "Die Rolle die einem User automatisch beim Joinen gegeben werden soll!").addOption(OptionType.ROLE, settingsSupJoinRoleOptionRole, "Join Rolle", true), new SubcommandData(settingsSupRemove, "Zeigt die eine Liste aller Option die Entfernt werden können!"), new SubcommandData(settingsSupWMessage, "Setzte einen Willkommens Channel um deine User willkommen zu heißen!").addOption(OptionType.CHANNEL, settingsSupWMessageOptionChannel, "Willkommens Channel", true));
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (e.getSubcommandName().equalsIgnoreCase(settingsSupJoinRole)) {
                try (Connection connection = Main.getMysql().getSource().getConnection()) {
                    String selectQuery = "SELECT count(*) FROM settings WHERE guild_id = ?;";
                    PreparedStatement statement = connection.prepareStatement(selectQuery);
                    statement.execute("USE `" + Main.getCustomConfig().getMysqlDatabase() + "`");
                    statement.setLong(1, e.getGuild().getIdLong());
                    Role role = Objects.requireNonNull(e.getOption(settingsSupJoinRoleOptionRole)).getAsRole();
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            String updateQuery = "UPDATE settings SET join_role = ? where guild_id = ?;";
                            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.execute("USE `" + Main.getCustomConfig().getMysqlDatabase() + "`");
                            updateStatement.setLong(1, role.getIdLong());
                            updateStatement.setLong(2, e.getGuild().getIdLong());
                            e.getInteraction().reply("Die Rolle fürs joinen wurde erfolgreich gesetzt!").queue();
                        } else {
                            String insertQuery = "INSERT INTO settings(guild_id, welcome_channel, join_role) VALUES (?, ?, ?);";
                            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                            insertStatement.execute("USE `" + Main.getCustomConfig().getMysqlDatabase() + "`");
                            insertStatement.setLong(1, e.getGuild().getIdLong());
                            insertStatement.setObject(2, null);
                            insertStatement.setLong(3, role.getIdLong());
                            insertStatement.execute();
                            e.getInteraction().reply("Die Rolle fürs joinen wurde erfolgreich gesetzt!").queue();
                        }
                    }
                } catch (SQLException exception) {
                    System.out.println(exception.getMessage());
                    Sentry.captureException(exception);
                }
            } else if (e.getSubcommandName().equalsIgnoreCase(settingsSupWMessage)) {
                e.deferReply().queue();
                try (Connection connection = Main.getMysql().getSource().getConnection()) {
                    String selectQuery = "SELECT count(*) FROM settings WHERE guild_id = ?;";
                    PreparedStatement statement = connection.prepareStatement(selectQuery);
                    statement.execute("USE `" + Main.getCustomConfig().getMysqlDatabase() + "`");
                    statement.setString(1, e.getGuild().getId());
                    TextChannel channel = e.getOption(settingsSupWMessageOptionChannel).getAsChannel().asTextChannel();
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            String updateQuery = "UPDATE settings SET welcome_channel = ? where guild_id = ?;";
                            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.execute("USE `" + Main.getCustomConfig().getMysqlDatabase() + "`");
                            updateStatement.setLong(1, channel.getIdLong());
                            updateStatement.setLong(2, e.getGuild().getIdLong());
                            e.getHook().sendMessage("Der Channel für die Willkommens Nachricht wurde erfolgreich gesetzt!").queue();
                        } else {
                            String insertQuery = "INSERT INTO settings(guild_id, welcome_channel, join_role) VALUES (?, ?, ?);";
                            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                            insertStatement.execute("USE `" + Main.getCustomConfig().getMysqlDatabase() + "`");
                            insertStatement.setLong(1, e.getGuild().getIdLong());
                            insertStatement.setLong(2, channel.getIdLong());
                            insertStatement.setObject(3, null);
                            insertStatement.execute();
                            e.getHook().sendMessage("Der Channel für die Willkommens Nachricht wurde erfolgreich gesetzt!").queue();
                        }
                    }
                } catch (SQLException exception) {
                    System.out.println(exception.getMessage());
                    Sentry.captureException(exception);
                }
            } else if (e.getSubcommandName().equalsIgnoreCase(settingsSupRemove)) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**Remove Option**");
                builder.setDescription("Entferne hier eine eingestellte Option für diesen Discord Server");
                e.getInteraction().replyEmbeds(builder.build()).addActionRow(StringSelectMenu.create("removeSelect").addOption("Join Role", settingsSupJoinRole).addOption("Willkommens Nachricht", settingsSupWMessage).build()).queue();
            }
        } else {
            e.getInteraction().reply("[ERROR]: Für den Command hast du nicht genügend Rechte").queue();
        }
    }
}
