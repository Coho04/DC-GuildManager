package de.goldendeveloper.guildmanager;

import de.goldendeveloper.dcbcore.DCBotBuilder;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import de.goldendeveloper.guildmanager.discord.commands.*;
import de.goldendeveloper.guildmanager.discord.events.CustomEvents;
import de.goldendeveloper.mysql.exceptions.NoConnectionException;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.stream.Stream;

public class Main {

    private static MysqlConnection mysqlConnection;

    public static void main(String[] args) throws NoConnectionException, SQLException {
        CustomConfig customConfig = new CustomConfig();

        DCBotBuilder dcBotBuilder = new DCBotBuilder(args, true);
        dcBotBuilder.registerCommands(new Ban(), new Birthday(), new Clear(), new JoinChannel(), new Kick(), new LeaveChannel(), new ServerOwner(), new ServerStats(), new Settings(), new TimeOut());
        dcBotBuilder.registerEvents(new CustomEvents());
        dcBotBuilder.build();

        mysqlConnection = new MysqlConnection(customConfig.getMysqlHostname(), customConfig.getMysqlUsername(), customConfig.getMysqlPassword(), customConfig.getMysqlPort());
    }

    public static MysqlConnection getMysqlConnection() {
        return mysqlConnection;
    }

    public static int getOnlineUsers(Guild guild) {
        return Long.valueOf(guild.getMembers().stream().filter(m -> m.getOnlineStatus() != OnlineStatus.OFFLINE && m.getOnlineStatus() != OnlineStatus.INVISIBLE).count()).intValue();
    }

    public static int getDoNotDisturbUsers(Guild guild) {
        return Long.valueOf(guild.getMembers().stream().filter(m -> m.getOnlineStatus() != OnlineStatus.DO_NOT_DISTURB).count()).intValue();
    }

    public static int getAfkUsers(Guild guild) {
        return Long.valueOf(guild.getMembers().stream().filter(m -> m.getOnlineStatus() != OnlineStatus.OFFLINE && m.getOnlineStatus() != OnlineStatus.INVISIBLE).count()).intValue();
    }

    public static int getOfflineUsers(Guild guild) {
        return Long.valueOf(guild.getMembers().stream().filter(m -> m.getOnlineStatus() == OnlineStatus.OFFLINE).count()).intValue();
    }
}