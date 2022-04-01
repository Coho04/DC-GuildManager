package de.goldendeveloper.guildmanager.events;

import de.goldendeveloper.guildmanager.commands.*;
import de.goldendeveloper.guildmanager.commands.admin.Restart;
import de.goldendeveloper.guildmanager.commands.admin.Shutdown;
import de.goldendeveloper.guildmanager.commands.moderate.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class RegisterCommands extends ListenerAdapter {

    public static final String Ban = "ban";
    public static final String settings = "settings";
    public static final String settingsSupJoinRole = "join-role";
    public static final String settingsSupJoinRoleOptionRole = "role";
    public static final String settingsSupRemove = "remove";
    public static final String settingsSupWMessage = "welcome-message";
    public static final String settingsSupWMessageOptionChannel = "channel";
    public static final String Kick = "kick";
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
            case Clear -> new Clear(e);
            case Ban -> new Ban(e);
            case Kick -> new Kick(e);
            case Ping -> new Ping(e);
            case Help -> new Help(e);
            case Invite -> new ServerInvite(e);
            case TimeOut -> new Timeout(e);
            case CmdRestart -> new Restart(e);
            case Birthday -> new Birthday(e);
            case BotStats -> new BotStats(e);
            case CmdShutdown -> new Shutdown(e);
            case Server_Owner -> new GuildOwner(e);
            case ServerStats -> new GuildStats(e);
            case Error_report -> new ErrorReport(e);
            case Join_Channel -> new ChannelJoin(e);
            case Leave_Channel -> new ChannelLeave(e);
            case Donate -> e.getInteraction().reply("Wenn du uns etwas Spenden möchtest dann kannst du dies gerne in dem du unten auf den Button kickst machen! \n" + "Vielen Danke <3 !").addActionRow(Button.link("https://donate.golden-developer.de/", "Zur Spende")).queue();
            case Bot_Owner -> e.getInteraction().reply("Der Bot Owner ist die Organisation Golden-Developer").addActionRow(Button.link("https://dc.golden-developer.de/", "Zum Server")).queue();
            case Join -> e.getInteraction().reply("Mit dem Button kannst du mich auf deinen Server einladen!").addActionRow(Button.link(e.getJDA().setRequiredScopes("applications.commands").getInviteUrl(Permission.ADMINISTRATOR), "Hier Klicken")).queue();
            case settings -> new Settings(e);
        }
    }
}
