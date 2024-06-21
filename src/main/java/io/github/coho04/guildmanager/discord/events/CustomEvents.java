package io.github.coho04.guildmanager.discord.events;

import io.github.coho04.guildmanager.Main;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class CustomEvents extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        try (Connection connection = Main.getMysql().getSource().getConnection()) {
            String selectQuery = "SELECT * FROM settings WHERE guild_id = ?;";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.execute("USE `GD-GuildManager`");
            statement.setString(1, e.getGuild().getId());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    long welcome_channel = rs.getLong("welcome_channel");
                    long join_role = rs.getLong("join_role");
                    TextChannel textChannel = e.getGuild().getTextChannelById(welcome_channel);
                    if (textChannel != null) {
                        User user = e.getMember().getUser();
                        String serverName = e.getGuild().getName();
                        EmbedBuilder emb = new EmbedBuilder();
                        emb.setColor(3447003);
                        emb.setTitle("**" + serverName + "**");
                        emb.setDescription("Willkommen **" + user.getName() + "**,\n" + " auf dem **" + serverName + "** Discord Server!");
                        emb.setTimestamp(new Date().toInstant());
                        emb.setThumbnail(user.getAvatarUrl());
                        emb.setFooter("@GuildManager");
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
                                em.setFooter("@GuildManager");
                                channel.sendMessageEmbeds(em.build()).queue();
                            });
                        }
                    }
                    Role role = e.getGuild().getRoleById(join_role);
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
                                em.setFooter("@GuildManager");
                                channel.sendMessageEmbeds(em.build()).queue();
                            });
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            Sentry.captureException(exception);
        }
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        try (Connection connection = Main.getMysql().getSource().getConnection()) {
            String selectQuery = "SELECT count(*) FROM settings WHERE guild_id = ?;";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.execute("USE `GD-GuildManager`");
            statement.setLong(1, Objects.requireNonNull(event.getGuild()).getIdLong());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    long join_role = rs.getLong("join_role");
                    long welcome_channel = rs.getLong("welcome_channel");
                    for (SelectOption option : event.getSelectedOptions()) {
                        if (option.getValue().equalsIgnoreCase("join-role")) {
                            if (join_role >= 1) {
                                PreparedStatement updateQuery = connection.prepareStatement("UPDATE settings SET join_role = ? where guild_id = ?  ");
                                updateQuery.execute("USE `GD-GuildManager`");
                                updateQuery.setObject(1, null);
                                updateQuery.setLong(1, event.getGuild().getIdLong());
                                updateQuery.execute();
                                event.getInteraction().reply("Die Einstellung für die Join Rolle wurde entfernt").queue();
                            } else {
                                event.getInteraction().reply("Es ist keine Einstellung mit dieser Option vorhanden!").queue();
                            }
                        } else if (option.getValue().equalsIgnoreCase("welcome-message")) {
                            if (welcome_channel >= 1) {
                                PreparedStatement updateQuery = connection.prepareStatement("UPDATE settings SET welcome_channel = ? where guild_id = ?  ");
                                updateQuery.execute("USE `GD-GuildManager`");
                                updateQuery.setObject(1, null);
                                updateQuery.setLong(1, event.getGuild().getIdLong());
                                updateQuery.execute();
                                event.getInteraction().reply("Die Einstellung für die Willkommens Nachricht wurde entfernt").queue();
                            } else {
                                event.getInteraction().reply("Es ist keine Einstellung mit dieser Option vorhanden!").queue();
                            }
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            Sentry.captureException(exception);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent e) {
        try {
            String cmd = e.getName();
            if (cmd.equalsIgnoreCase("ban")) {
                if (e.getFocusedOption().getName().equalsIgnoreCase("time")) {
                    e.getInteraction().replyChoices(new Command.Choice("Permanent", 9999), new Command.Choice("14 Tag", 14), new Command.Choice("7 Tag", 7), new Command.Choice("5 Tag", 5), new Command.Choice("3 Tag", 3), new Command.Choice("2 Tag", 2), new Command.Choice("1 Tag", 1)).queue();
                }
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            Sentry.captureException(exception);
        }
    }
}
