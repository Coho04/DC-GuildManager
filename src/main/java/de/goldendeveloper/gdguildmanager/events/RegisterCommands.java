package de.goldendeveloper.gdguildmanager.events;

import de.goldendeveloper.gdguildmanager.commands.*;
import de.goldendeveloper.gdguildmanager.commands.admin.CMD_BOT_Activity;
import de.goldendeveloper.gdguildmanager.commands.admin.CMD_BOT_Stop;
import de.goldendeveloper.gdguildmanager.Main;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class RegisterCommands extends ListenerAdapter {

    private static final String CMD_Ban_s = "ban";
    private static final String CMD_Kick_s = "kick";
    private static final String CMD_Donate_s = "donate";
    private static final String CMD_Ping_s = "ping";
    private static final String CMD_Error_report_s = "error-report";
    private static final String CMD_Birthday_s = "birthday";
    private static final String CMD_Invite_s = "invite";
    private static final String CMD_Join_s = "join";
    private static final String CMD_Leave_s = "leave";
    private static final String CMD_Leave_Channel_s = "leave-channel";
    private static final String CMD_Bot_Owner_s = "bot-owner";
    private static final String CMD_Join_Channel_s = "join-channel";
    private static final String CMD_Server_Owner_s = "server-owner";
    private static final String CMD_Help_s = "help";
    private static final String CMD_Stop_s = "stop";
    private static final String CMD_TimeOut_s = "timeout";
    private static final String CMD_Activity_s = "activity";
    private static final String CMD_Server_Stats_s = "server-stats";
    private static final String CMD_Bot_Stats_s = "bot-stats";

    private static final String CMD_Create_Stats_Channel_s = "create-stats-channel";
    private static final String CMD_Delete_Stats_Channel_s = "create-stats-channel";
    private static final String CMD_Create_Stats_Category_s = "create-stats-channel";
    private static final String CMD_Delete_Stats_Category_s = "create-stats-channel";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        String cmd = e.getName();
        if (cmd.equalsIgnoreCase(CMD_Ban_s)) {
            CMD_Ban.onBanCommand(e);
        } else if (cmd.equalsIgnoreCase(CMD_Kick_s)) {
            CMD_Kick.onKickCommand(e);
        } else if (cmd.equalsIgnoreCase(CMD_Leave_s)) {
            CMD_Guild_Leave.onLeaveGuild(e);
        } else if (cmd.equalsIgnoreCase(CMD_Leave_Channel_s)) {
            CMD_Channel_Leave.onLeaveChannel(e);
        } else if (cmd.equalsIgnoreCase(CMD_Join_Channel_s)) {
            CMD_Channel_Join.onJoinChannel(e);
        } else if (cmd.equalsIgnoreCase(CMD_Server_Owner_s)) {
            CMD_Guild_Owner.onGuildOwner(e);
        } else if (cmd.equalsIgnoreCase(CMD_Join_s)) {
            CMD_BOT_Join.onBotJoin(e);
        } else if (cmd.equalsIgnoreCase(CMD_Invite_s)) {
            CMD_Invite.onInvite(e);
        } else if (cmd.equalsIgnoreCase(CMD_Ping_s))  {
            CMD_Ping.onPing(e);
        } else if (cmd.equalsIgnoreCase(CMD_Donate_s)) {
            CMD_Donate.onDonate(e);
        } else if (cmd.equalsIgnoreCase(CMD_Error_report_s)) {
            CMD_Error_report.onErrorReport(e);
        } else if (cmd.equalsIgnoreCase(CMD_Birthday_s)) {
            CMD_Birthday.onBirthday(e);
        } else if (cmd.equalsIgnoreCase(CMD_Bot_Owner_s)) {
            CMD_BOT_Owner.onBotOwner(e);
        } else if (cmd.equalsIgnoreCase(CMD_Help_s)) {
            CMD_Help.onHelp(e);
        } else if (cmd.equalsIgnoreCase(CMD_Stop_s)) {
            CMD_BOT_Stop.onBotStop(e);
        } else if (cmd.equalsIgnoreCase(CMD_Activity_s)) {
            CMD_BOT_Activity.onActivity(e);
        } else if (cmd.equalsIgnoreCase(CMD_TimeOut_s)) {
            CMD_Timeout.onTimeout(e);
        } else if (cmd.equalsIgnoreCase(CMD_Server_Stats_s)) {
            CMD_Server_Stats.onServerStats(e);
        } else if (cmd.equalsIgnoreCase(CMD_Bot_Stats_s)) {
            CMD_BOT_Stats.onBotStats(e);
        }
    }

    public static void registerCommand() {
        Main.Bot.upsertCommand(CMD_Leave_s, "Lässt den Bot den Server verlassen").queue();
        Main.Bot.upsertCommand(CMD_Server_Owner_s, "Nennt den Server Inhaber").queue();
        Main.Bot.upsertCommand(CMD_Join_s, "Der Bot sendet dir einen link um ihn einzuladen").queue();
        Main.Bot.upsertCommand(CMD_Invite_s, "Du wirst eingeladen auf unseren Discord").queue();
        Main.Bot.upsertCommand(CMD_Join_Channel_s, "Der Bot betritt deinen Voice Channel").queue();
        Main.Bot.upsertCommand(CMD_Leave_Channel_s, "Der Bot verlässt deinen Voice Channel").queue();
//        Main.Bot.upsertCommand("clear", "Löscht eine Anzahl von Nachrichten").addOption(OptionType.INTEGER, "anzahl", "Eine Anzahl von Nachrichten die Gelöscht werden sollen", true).queue();
        Main.Bot.upsertCommand(CMD_Help_s, "Zeigt eine Liste von möglichen Befehlen").queue();
        Main.Bot.upsertCommand(CMD_Birthday_s, "Gratuliere einem anderen User").addOption(OptionType.USER, "user", "Fügt einen anderen User hinzu", true).addOption(OptionType.BOOLEAN, "private", "Möchtest du dem User die Glückwünsche privat zukommen lassen?").queue();
        Main.Bot.upsertCommand(CMD_Bot_Owner_s, "Zeigt dir den Bot Programmierer").queue();
        Main.Bot.upsertCommand(CMD_Error_report_s, "Reporte einen Bot fehler").addOption(OptionType.STRING, "error", "Schildere hier deinen gefundenen Bot Fehler", true).queue();
        Main.Bot.upsertCommand(CMD_Ping_s, "Antwortet mit Pong").queue();
        Main.Bot.upsertCommand(CMD_Donate_s, "Zeigt dir eine Spende möglichkeit").queue();

        Main.Bot.upsertCommand(CMD_Server_Stats_s, "Zeigt dir die Stats des Servers").queue();
        Main.Bot.upsertCommand(CMD_Bot_Stats_s, "Zeigt dir die Stats des Bots").queue();
        Main.Bot.upsertCommand(CMD_Donate_s, "Zeigt dir eine Spende möglichkeit").queue();
        //        Main.Bot.upsertCommand("say", "Sagt etwas").queue();

//        Main.Bot.upsertCommand(CMD_Create_Stats_Channel_s, "Bannt einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.INTEGER, "time", "Gib die Ban Dauer in Tagen an um den User zu bannen", true).addOption(OptionType.STRING, "reason", "Begründe deinen Ban", true).queue();
        Main.Bot.upsertCommand(CMD_Ban_s, "Bannt einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.INTEGER, "time", "Gib die Ban Dauer in Tagen an um den User zu bannen", true).addOption(OptionType.STRING, "reason", "Begründe deinen Ban", true).queue();
        Main.Bot.upsertCommand(CMD_Kick_s, "Kickt einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.STRING, "reason", "Begründe deinen Kick", true).queue();
        Main.Bot.upsertCommand(CMD_TimeOut_s, "Timeoutet einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.STRING, "time", "Gib die Timeout Dauer in Tagen an um den User zu timeouten. (In Minuten)", true).queue();
//        Main.Bot.upsertCommand("warn", "Warnt einen bestimmten Spieler").addOption(OptionType.USER, "user", "Fügt einen Benutzer hinzu", true).addOption(OptionType.STRING, "reason", "Begründe die Warnung").queue();



//        Main.Bot.upsertCommand("restart", "Startet den Bot Neu").queue();
        Main.Bot.upsertCommand(CMD_Stop_s, "Stoppt den Bot").queue();
        Main.Bot.upsertCommand(CMD_Activity_s, "Ändert die Aktivität des Bots").addOption(OptionType.STRING, "type", "playing watching competing streaming listening custom", true).addOption(OptionType.STRING, "activity", "Hier kommt die Aktivität hin", true).addOption(OptionType.STRING, "url", "Füge deiner Option eine URL hinzu").queue();

//        Main.Bot.upsertCommand("eight-ball", "Gibt die eine zufällige Antwort").queue();
//        Main.Bot.upsertCommand("sethomechannel","").queue();
//        Main.Bot.upsertCommand("team", "Zeigt dir das Server Team").queue();
//        Main.Bot.upsertCommand("social-media", "Lässt eine Liste von Social Media Links anzeigen").queue();
//        Main.Bot.upsertCommand("twitter", "Zeigt dir unseren Twitter Link").queue();
//        Main.Bot.upsertCommand("twitch", "Zeigt dir unseren Twitch Link").queue();
//        Main.Bot.upsertCommand("instagram", "Zeigt dir unseren Instagram Link").queue();
//        Main.Bot.upsertCommand("tiktok", "Zeigt dir unseren TikTok Link").queue();
//        Main.Bot.upsertCommand("youtube", "Zeigt dir unseren YouTube Link").queue();
//        Main.Bot.upsertCommand("fact", "Gibt dir einen Fakt wieder").queue();
//        Main.Bot.upsertCommand("film", "Empfiehlt die einen Film").queue();
//        Main.Bot.upsertCommand("joke", "Erzählt dir einen Witz").queue();
//        Main.Bot.upsertCommand("serien", "Empfiehlt die eine Serie").queue();
    }
}
