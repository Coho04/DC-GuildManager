package de.goldendeveloper.guildmanager.events;

import de.goldendeveloper.guildmanager.Main;
import de.goldendeveloper.mysql.entities.Row;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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
                            User user = e.getMember().getUser();
                            String ServerName = e.getGuild().getName();
                            MessageEmbed emb = new EmbedBuilder()
                                    .setColor(3447003)
                                    .setTitle("**" + ServerName + "**")
                                    .setDescription("Willkommen **" + user.getName() + "**,\n" +
                                            " auf dem **" + ServerName + "** Discord Server!")
                                    .setTimestamp(Main.getDiscord().getDate())
                                    .setThumbnail(user.getAvatarUrl())
                                    .setFooter("@Golden-Developer")
                                    .build();
                            TextChannel ch = e.getGuild().getTextChannelById(row.get(Main.colmWChannel).toString());
                            if (ch != null) {
                                ch.sendMessageEmbeds(emb).queue();
                            } else {
                                e.getGuild().getOwner().getUser().openPrivateChannel().queue(channel->{
                                    MessageEmbed em = new EmbedBuilder()
                                            .setColor(Color.RED)
                                            .setTitle("**ERROR**")
                                            .setDescription("Der angegebene Willkommens Channel konnte auf **" + e.getGuild().getName() +  "** nicht gefunden werden!")
                                            .setTimestamp(Main.getDiscord().getDate())
                                            .setFooter("@Golden-Developer")
                                            .build();
                                    channel.sendMessageEmbeds(em).queue();
                                });
                            }
                        } else if (!row.get(Main.colmJRole).toString().isEmpty() || !row.get(Main.colmJRole).toString().isBlank()) {
                            Role role = e.getGuild().getRoleById(row.get(Main.colmJRole).toString());
                            if (role != null) {
                                e.getGuild().addRoleToMember(e.getMember(), role).queue();
                            } else {
                                e.getGuild().getOwner().getUser().openPrivateChannel().queue(channel->{
                                    MessageEmbed em = new EmbedBuilder()
                                            .setColor(Color.RED)
                                            .setTitle("**ERROR**")
                                            .setDescription("Die angegebene Join Rolle konnte auf **" + e.getGuild().getName() +  "** nicht gefunden werden!")
                                            .setTimestamp(Main.getDiscord().getDate())
                                            .setFooter("@Golden-Developer")
                                            .build();
                                    channel.sendMessageEmbeds(em).queue();
                                });
                            }
                        }
                    }
                }
            }
        }
    }
}
