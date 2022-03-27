package de.goldendeveloper.guildmanager.events;

import de.goldendeveloper.guildmanager.commands.*;
import de.goldendeveloper.guildmanager.commands.admin.CMD_BOT_Activity;
import de.goldendeveloper.guildmanager.commands.admin.CMD_BOT_Stop;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class RegisterCommands extends ListenerAdapter {

    public static final String CMD_Ban_s = "ban";
    public static final String CMD_Kick_s = "kick";
    public static final String CMD_Donate_s = "donate";
    public static final String CMD_Ping_s = "ping";
    public static final String CMD_Error_report_s = "error-report";
    public static final String CMD_Birthday_s = "birthday";
    public static final String CMD_Invite_s = "invite";
    public static final String CMD_Join_s = "join";
    public static final String CMD_Leave_s = "leave";
    public static final String CMD_Leave_Channel_s = "leave-channel";
    public static final String CMD_Bot_Owner_s = "bot-owner";
    public static final String CMD_Join_Channel_s = "join-channel";
    public static final String CMD_Server_Owner_s = "server-owner";
    public static final String CMD_Help_s = "help";
    public static final String CMD_Stop_s = "stop";
    public static final String CMD_TimeOut_s = "timeout";
    public static final String CMD_Activity_s = "activity";
    public static final String CMD_Server_Stats_s = "server-stats";
    public static final String CMD_Bot_Stats_s = "bot-stats";

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
            e.getInteraction().reply("Der Bot Owner ist die Organisation Golden-Developer").addActionRow(Button.link("https://discord.gg/", "Zum Server")).queue();
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
}
