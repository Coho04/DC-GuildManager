package de.goldendeveloper.guildmanager.discord.events;

import de.goldendeveloper.guildmanager.MysqlConnection;
import de.goldendeveloper.guildmanager.Main;
import de.goldendeveloper.mysql.entities.RowBuilder;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RegisterCommands extends ListenerAdapter {

    public static String _Coho04_MEMBER = "513306244371447828";
    public static String hasNoPermissions = "[ERROR]: Für den Command hast du nicht genügend Rechte";
    public static final String Ban = "ban";
    public static final String BanOptionTime = "time";
    public static final String BanOptionUser = "user";
    public static final String BanOptionReason = "reason";
    public static final String settings = "settings";
    public static final String settingsSupJoinRole = "join-role";
    public static final String settingsSupJoinRoleOptionRole = "role";
    public static final String settingsSupRemove = "remove";
    public static final String settingsSupWMessage = "welcome-message";
    public static final String settingsSupWMessageOptionChannel = "channel";
    public static final String Kick = "kick";
    public static final String KickOptionReason = "reason";
    public static final String Donate = "donate";
    public static final String Ping = "ping";
    public static final String Error_report = "error-report";
    public static final String Birthday = "birthday";
    public static final String Invite = "invite";
    public static final String Join = "join";
    public static final String Leave_Channel = "leave-channel";
    public static final String Bot_Owner = "bot-owner";
    public static final String Join_Channel = "join-channel";
    public static final String Server_Owner = "server-owner";
    public static final String Help = "help";
    public static final String TimeOut = "timeout";
    public static final String TimeOutOptionUser = "user";
    public static final String TimeOutOptionTime = "time";
    public static final String ServerStats = "server-stats";
    public static final String BotStats = "bot-stats";
    public static final String Clear = "clear";
    public static final String ClearOptionAmount = "anzahl";
    public static final String CmdShutdown = "shutdown";
    public static final String CmdRestart = "restart";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        String cmd = e.getName();
        switch (cmd) {
            case Clear -> this.Clear(e);
            case Ban -> this.Ban(e);
            case Kick -> this.Kick(e);
            case Ping -> this.Ping(e);
            case Help -> this.Help(e);
            case Invite -> this.ServerInvite(e);
            case TimeOut -> this.Timeout(e);
            case CmdRestart -> this.Restart(e);
            case Birthday -> this.Birthday(e);
            case BotStats -> this.BotStats(e);
            case CmdShutdown -> this.Shutdown(e);
            case Server_Owner -> this.GuildOwner(e);
            case ServerStats -> this.GuildStats(e);
            case Error_report -> this.ErrorReport(e);
            case Join_Channel -> this.ChannelJoin(e);
            case Leave_Channel -> this.ChannelLeave(e);
            case Donate -> e.getInteraction().reply("Wenn du uns etwas Spenden möchtest dann kannst du dies gerne in dem du unten auf den Button kickst machen! \n" + "Vielen Danke <3 !").addActionRow(Button.link("https://donate.golden-developer.de/", "Zur Spende")).queue();
            case Bot_Owner -> e.getInteraction().reply("Der Bot Owner ist die Organisation Golden-Developer").addActionRow(Button.link("https://dc.golden-developer.de/", "Zum Server")).queue();
            case Join -> e.getInteraction().reply("Mit dem Button kannst du mich auf deinen Server einladen!").addActionRow(Button.link(e.getJDA().setRequiredScopes("applications.commands").getInviteUrl(Permission.ADMINISTRATOR), "Hier Klicken")).queue();
            case settings -> this.Settings(e);
        }
    }

    public void Timeout(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.ADMINISTRATOR)) {
                Member member = e.getOption(TimeOutOptionUser).getAsMember();
                long time = e.getOption(TimeOutOptionTime).getAsLong();
                if (member != null) {
                    if (time >= 0) {
                        member.timeoutFor(time, TimeUnit.MINUTES).queue();
                        e.getInteraction().reply("Der User " + member.getUser().getName() + " hat erfolgreich einen timeout bekommen!").queue();
                    } else {
                        e.getInteraction().reply(hasError("Timeout Dauer is 0")).queue();
                    }
                } else {
                    e.getInteraction().reply(hasError("User ist NULL")).queue();
                }
            } else {
                e.getInteraction().reply(hasNoPermissions).queue();
            }
        } else {
            e.getInteraction().reply(hasError("CMD User ist NULL")).queue();
        }
    }

    public void Settings(SlashCommandInteractionEvent e) {
        if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (e.getSubcommandName().equalsIgnoreCase(RegisterCommands.settingsSupJoinRole)) {
                if (Main.getCreateMysql().getMysql().existsDatabase(MysqlConnection.dbName)) {
                    if (Main.getCreateMysql().getMysql().getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.settingsTName)) {
                        Table table = Main.getCreateMysql().getMysql().getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.settingsTName);
                        if (table.existsColumn(MysqlConnection.colmGuild)) {
                            Role role = e.getOption(RegisterCommands.settingsSupJoinRoleOptionRole).getAsRole();
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
                        } else {
                            Main.getDiscord().sendErrorMessage("Column " + MysqlConnection.colmGuild + " in " + MysqlConnection.settingsTName + " existiert nicht");
                        }
                    } else {
                        Main.getDiscord().sendErrorMessage("Table " + MysqlConnection.settingsTName + " in " + MysqlConnection.dbName + " existiert nicht");
                    }
                } else {
                    Main.getDiscord().sendErrorMessage("Database " + MysqlConnection.dbName + " existiert nicht");
                }
            } else if (e.getSubcommandName().equalsIgnoreCase(RegisterCommands.settingsSupWMessage)) {
                e.deferReply().queue();
                if (Main.getCreateMysql().getMysql().existsDatabase(MysqlConnection.dbName)) {
                    if (Main.getCreateMysql().getMysql().getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.settingsTName)) {
                        Table table = Main.getCreateMysql().getMysql().getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.settingsTName);
                        if (table.existsColumn(MysqlConnection.colmGuild)) {
                            TextChannel channel = e.getOption(RegisterCommands.settingsSupWMessageOptionChannel).getAsChannel().asTextChannel();
                            if (channel != null) {
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
                            } else {
                                e.getHook().sendMessage("Der Angegebene Channel konnte nicht gefunden werden!").queue();
                            }
                        } else {
                            Main.getDiscord().sendErrorMessage("Column " + MysqlConnection.colmGuild + " in " + MysqlConnection.settingsTName + " existiert nicht");
                        }
                    } else {
                        Main.getDiscord().sendErrorMessage("Table " + MysqlConnection.settingsTName + " in " + MysqlConnection.dbName + " existiert nicht");
                    }
                } else {
                    Main.getDiscord().sendErrorMessage("Database " + MysqlConnection.dbName + " existiert nicht");
                }
            } else if (e.getSubcommandName().equalsIgnoreCase(RegisterCommands.settingsSupRemove)) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**Remove Option**");
                builder.setDescription("Entferne hier eine eingestellte Option für diesen Discord Server");
                e.getInteraction().replyEmbeds(builder.build()).addActionRow(
                        SelectMenu.create("removeSelect")
                                .addOption("Join Role", RegisterCommands.settingsSupJoinRole)
                                .addOption("Willkommens Nachricht", RegisterCommands.settingsSupWMessage)
                                .build()
                ).queue();
            }
        } else {
            e.getInteraction().reply(hasNoPermissions).queue();
        }
    }

    public void Kick(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.KICK_MEMBERS) || m.hasPermission(Permission.ADMINISTRATOR)) {
                Member member = e.getOption("user").getAsMember();
                String reason = "";
                if (e.getOption(KickOptionReason) != null) {
                    reason = e.getOption(KickOptionReason).getAsString();
                }
                if (member != null) {
                    if (!reason.isEmpty()) {
                        member.kick(reason).queue();
                        e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gekickt!").queue();
                    } else {
                        member.kick().queue();
                        e.getInteraction().reply("Der User " + member.getUser().getName() + " wurde erfolgreich gekickt!").queue();
                    }
                } else {
                    e.getInteraction().reply(hasError("User ist NULL")).queue();
                }
            } else {
                e.getInteraction().reply(hasNoPermissions).queue();
            }
        } else {
            e.getInteraction().reply(hasError("CMD User ist NULL")).queue();
        }
    }

    public void Clear(SlashCommandInteractionEvent e) {
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
                e.getInteraction().reply(hasError("CMD User ist NULL")).queue();
            }
        }
    }

    public void Ban(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            if (m.hasPermission(Permission.BAN_MEMBERS) || m.hasPermission(Permission.ADMINISTRATOR)) {
                Member member = e.getOption(BanOptionUser).getAsMember();
                if (e.getOption(BanOptionTime) != null) {
                    int time = e.getOption(BanOptionTime).getAsInt();
                    String reason = "";
                    if (e.getOption(BanOptionReason) != null) {
                        reason = e.getOption(BanOptionReason).getAsString();
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
                        e.getInteraction().reply(hasError("User ist NULL")).queue();
                    }
                } else {
                    e.getInteraction().reply(hasError("Zeit ist NULL")).queue();
                }
            } else {
                e.getInteraction().reply(hasNoPermissions).queue();
            }
        } else {
            e.getInteraction().reply(hasError("CMD User ist NULL")).queue();
        }
    }

    public void Shutdown(SlashCommandInteractionEvent e) {
        User _Coho04_ = e.getJDA().getUserById(_Coho04_MEMBER);
        User zRazzer = e.getJDA().getUserById("428811057700536331");
        if (e.getUser() == zRazzer || e.getUser() == _Coho04_) {
            e.getInteraction().reply("Der Discord Bot [" + e.getJDA().getSelfUser().getName() + "] wird nun heruntergefahren").queue();
            e.getJDA().shutdown();
        } else {
            e.getInteraction().reply("Dazu hast du keine Rechte du musst für diesen Befehl der Bot Inhaber sein!").queue();
        }
    }

    public void Restart(SlashCommandInteractionEvent e) {
        User _Coho04_ = e.getJDA().getUserById(_Coho04_MEMBER);
        User zRazzer = e.getJDA().getUserById("428811057700536331");
        if (e.getUser() == zRazzer || e.getUser() == _Coho04_) {
            try {
                e.getInteraction().reply("Der Discord Bot [" + e.getJDA().getSelfUser().getName() + "] wird nun neugestartet!").queue();
                Process p = Runtime.getRuntime().exec("screen -AmdS " + Main.getDiscord().getProjektName() + " java -Xms1096M -Xmx1096M -jar " + Main.getDiscord().getProjektName() + "-" + Main.getDiscord().getProjektVersion() + ".jar restart");
                p.waitFor();
                e.getJDA().shutdown();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            e.getInteraction().reply("Dazu hast du keine Rechte du musst für diesen Befehl der Bot inhaber sein!").queue();
        }
    }

    public void ServerInvite(SlashCommandInteractionEvent e) {
        Guild g = e.getJDA().getGuildById(Main.getConfig().getDiscordServer());
        if (g != null) {
            e.getInteraction().reply("Mit dem Button unten kannst du unserem Server beitreten!")
                    .addActionRow(Button.link(g.getTextChannelById(747208323761176586L).createInvite().complete().getUrl(), "Zum Server"))
                    .queue();
        } else {
            e.getInteraction().reply(hasError("Guild is NULL")).queue();
        }
    }

    public void Ping(SlashCommandInteractionEvent e) {
        long time = System.currentTimeMillis();
        e.getInteraction().reply("Pong!").queue(response -> response.sendMessage("Pong:" + System.currentTimeMillis() + time + " ms").queue());
    }

    public void Help(SlashCommandInteractionEvent e) {
        List<Command> cmd = Main.getDiscord().getBot().retrieveCommands().complete();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Help Commands**");
        embed.setColor(Color.MAGENTA);
        embed.setFooter("@Golden-Developer", e.getJDA().getSelfUser().getAvatarUrl());
        for (Command c : cmd) {
            embed.addField("/" + c.getName(), c.getDescription(), true);
        }
        e.getInteraction().replyEmbeds(embed.build())
                .addActionRow(
                        Button.link("https://wiki.Golden-Developer.de", "Online Übersicht"),
                        Button.link("https://support.Golden-Developer.de", "Support Anfragen")
                ).queue();
    }

    public void GuildStats(SlashCommandInteractionEvent e) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("**Server Stats**");
        embedBuilder.setFooter("@Golden-Developer", e.getJDA().getSelfUser().getAvatarUrl());
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.addField("Alle Users", String.valueOf(e.getGuild().getMembers().size()), true);
        embedBuilder.addField("Online Users", String.valueOf(getOnlineUsers(e.getGuild())), true);
        embedBuilder.addField("Offline Users", String.valueOf(getOfflineUsers(e.getGuild())), true);
        embedBuilder.addField("AFK Users", String.valueOf(getAfkUsers(e.getGuild())), true);
        embedBuilder.addField("Nicht Stören Users", String.valueOf(getDoNotDisturbUsers(e.getGuild())), true);
        embedBuilder.addField("Channel", String.valueOf(e.getGuild().getChannels().size()), true);
        embedBuilder.addField("TextChannel", String.valueOf(e.getGuild().getTextChannels().size()), true);
        embedBuilder.addField("VoiceChannel", String.valueOf(e.getGuild().getVoiceChannels().size()), true);
        embedBuilder.addField("Server Owner", e.getGuild().getOwner().getUser().getName(), true);
        e.getInteraction().replyEmbeds(embedBuilder.build()).queue();
    }

    public void BotStats(SlashCommandInteractionEvent e) {
        Guild MainServer = e.getJDA().getGuildById(Main.getConfig().getDiscordServer());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Server Stats**");
        embed.setFooter("@Golden-Developer", e.getJDA().getSelfUser().getAvatarUrl());
        embed.setColor(Color.MAGENTA);
        embed.addField("Server", String.valueOf(e.getJDA().getGuilds().size()), true);
        embed.addField("Support-Server", MainServer.getName(), true);
        embed.addField("Bot-Owner", "@Golden-Developer", true);
        e.getInteraction().replyEmbeds(embed.build()).addActionRow(Button.link(MainServer.getDefaultChannel().createInvite().complete().getUrl(), MainServer.getName())).queue();
    }

    public void Birthday(SlashCommandInteractionEvent e) {
        Member member = e.getMember();
        if (member != null) {
            User u = e.getOption("user").getAsUser();
            if (e.getOption("private").getAsBoolean()) {
                u.openPrivateChannel().queue(msg -> {
                    msg.sendMessage("Herzlichen Glückwunsch zum Geburtstag wünscht dir " + member.getUser().getName() + ". Viel Spaß dir heute!!!").queue();
                });
            } else {
                e.getChannel().sendMessage(u.getAsMention() + "Herzlichen Glückwunsch zum Geburtstag wünscht dir " + member.getUser().getName() + ". Viel Spaß dir heute!!!").queue();
            }
            e.getInteraction().reply("Dem User wurde erfolgreich gratuliert!").queue();
        } else {
            e.getInteraction().reply(hasError("CMD Member ist NULL")).queue();
        }
    }

    public void GuildOwner(SlashCommandInteractionEvent e) {
        Guild g = e.getGuild();
        if (g != null) {
            if (g.getOwner() != null) {
                Member m = g.getOwner();
                if (m != null) {
                    User u = m.getUser();
                    if (!u.getName().isEmpty()) {
                        e.getInteraction().reply("Der Serverinhaber ist: " + e.getGuild().getOwner().getUser().getName()).queue();
                    } else {
                        e.getInteraction().reply(hasError("Owner Name ist NULL")).queue();
                    }
                } else {
                    e.getInteraction().reply(hasError("Owner Member ist NULL")).queue();
                }
            } else {
                e.getInteraction().reply(hasError("Owner ist NULL")).queue();
            }
        } else {
            e.getInteraction().reply(hasError("Guild ist NULL")).queue();
        }
    }

    public void ErrorReport(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            User c = e.getJDA().getUserById(_Coho04_MEMBER);
            if (c != null) {
                c.openPrivateChannel().queue(msg -> {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle(e.getUser().getName());
                    embed.setImage(e.getUser().getAvatarUrl());
                    embed.addField("ERROR", e.getOption("error").getAsString(), true);
                    msg.sendMessageEmbeds(embed.build()).queue();
                });
                e.getInteraction().reply("Dein Report wurde erfolgreich abgesendet").queue();
            } else {
                e.getInteraction().reply(hasError("_Coho04_ is NULL")).queue();
            }
        }
    }

    public void ChannelLeave(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            Guild g = e.getGuild();
            if (g != null) {
                GuildVoiceState voice = g.getSelfMember().getVoiceState();
                if (voice != null) {
                    if (voice.getChannel() != null) {
                        if (voice.inAudioChannel()) {
                            e.getJDA().getDirectAudioController().disconnect(g);
                            e.getInteraction().reply("Ich habe erfolgreich deinen Channel verlassen!").queue();
                        } else {
                            e.getInteraction().reply(hasError("Du bist in keinem VoiceChannel!")).queue();
                        }
                    } else {
                        e.getInteraction().reply(hasError("Voice Channel is NULL")).queue();
                    }
                } else {
                    e.getInteraction().reply(hasError("VoiceState is NULL")).queue();
                }
            } else {
                e.getInteraction().reply(hasError("Guild is NULL")).queue();
            }
        } else {
            e.getInteraction().reply(hasError("MEMBER is NULL")).queue();
        }
    }

    public void ChannelJoin(SlashCommandInteractionEvent e) {
        Member m = e.getMember();
        if (m != null) {
            GuildVoiceState voice = m.getVoiceState();
            if (voice != null) {
                if (voice.getChannel() != null) {
                    if (voice.inAudioChannel()) {
                        e.getJDA().getDirectAudioController().connect(voice.getChannel());
                        e.getInteraction().reply("Ich habe erfolgreich deinen Channel betreten!").queue();
                    } else {
                        e.getInteraction().reply(hasError("Du bist in keinem VoiceChannel!")).queue();
                    }
                } else {
                    e.getInteraction().reply(hasError("Voice Channel is NULL")).queue();
                }
            } else {
                e.getInteraction().reply(hasError("VoiceState is NULL")).queue();
            }
        } else {
            e.getInteraction().reply(hasError("Member is NULL")).queue();
        }
    }

    public static int getOnlineUsers(Guild guild) {
        int onlineMembers = 0;
        for (Member m : guild.getMembers()) {
            if (m.getOnlineStatus() != OnlineStatus.OFFLINE && m.getOnlineStatus() != OnlineStatus.INVISIBLE) {
                onlineMembers++;
            }
        }
        return onlineMembers;
    }

    public static int getDoNotDisturbUsers(Guild guild) {
        int doNotDisturbMembers = 0;
        for (Member m : guild.getMembers()) {
            if (m.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
                doNotDisturbMembers++;
            }
        }
        return doNotDisturbMembers;
    }

    public static int getAfkUsers(Guild guild) {
        int afkMembers = 0;
        for (Member m : guild.getMembers()) {
            if (m.getOnlineStatus() != OnlineStatus.OFFLINE && m.getOnlineStatus() != OnlineStatus.INVISIBLE) {
                afkMembers++;
            }
        }
        return afkMembers;
    }

    public static int getOfflineUsers(Guild guild) {
        int OfflineMembers = 0;
        for (Member m : guild.getMembers()) {
            if (m.getOnlineStatus() == OnlineStatus.OFFLINE) {
                OfflineMembers++;
            }
        }
        return OfflineMembers;
    }

    public static String hasError(String error) {
        return "[ERROR]: Es ist ein Fehler aufgetreten bitte melden den Grund mit /error-report \n" + "Fehler: " + error;
    }
}
