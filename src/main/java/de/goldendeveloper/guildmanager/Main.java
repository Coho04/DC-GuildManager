package de.goldendeveloper.guildmanager;

import de.goldendeveloper.dcbcore.DCBotBuilder;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import de.goldendeveloper.guildmanager.discord.commands.*;
import de.goldendeveloper.guildmanager.discord.events.CustomEvents;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Main {

    private static MysqlConnection mysqlConnection;

    public static void main(String[] args) {
        CustomConfig customConfig = new CustomConfig();

        DCBotBuilder dcBotBuilder = new DCBotBuilder(args);
        dcBotBuilder.registerCommands(getCustomCommands());
        dcBotBuilder.registerEvents(new CustomEvents());
        dcBotBuilder.build();

        mysqlConnection = new MysqlConnection(customConfig.getMysqlHostname(), customConfig.getMysqlUsername(), customConfig.getMysqlPassword(), customConfig.getMysqlPort());
    }

    public static LinkedList<CommandInterface> getCustomCommands() {
        LinkedList<CommandInterface> commandDataList = new LinkedList<>();
        commandDataList.add(new Ban());
        commandDataList.add(new Birthday());
        commandDataList.add(new Clear());
        commandDataList.add(new JoinChannel());
        commandDataList.add(new Kick());
        commandDataList.add(new LeaveChannel());
        commandDataList.add(new ServerOwner());
        commandDataList.add(new ServerStats());
        commandDataList.add(new Settings());
        commandDataList.add(new TimeOut());
        return commandDataList;
    }

    public static MysqlConnection getMysqlConnection() {
        return mysqlConnection;
    }

    public static int getOnlineUsers(Guild guild) {
        Stream<Member> members = guild.getMembers().stream().filter(m -> m.getOnlineStatus() != OnlineStatus.OFFLINE && m.getOnlineStatus() != OnlineStatus.INVISIBLE);
        return Long.valueOf(members.count()).intValue();
    }

    public static int getDoNotDisturbUsers(Guild guild) {
        Stream<Member> members = guild.getMembers().stream().filter(m -> m.getOnlineStatus() != OnlineStatus.DO_NOT_DISTURB);
        return Long.valueOf(members.count()).intValue();
    }

    public static int getAfkUsers(Guild guild) {
        Stream<Member> members = guild.getMembers().stream().filter(m ->m.getOnlineStatus() != OnlineStatus.OFFLINE && m.getOnlineStatus() != OnlineStatus.INVISIBLE);
        return Long.valueOf(members.count()).intValue();
    }

    public static int getOfflineUsers(Guild guild) {
        Stream<Member> members = guild.getMembers().stream().filter(m ->m.getOnlineStatus() == OnlineStatus.OFFLINE);
        return Long.valueOf(members.count()).intValue();
    }
}