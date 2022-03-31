package de.goldendeveloper.guildmanager;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class ID {

    public static String _Coho04_MEMBER = "513306244371447828";
    public static String hasNoPermissions = "[ERROR]: Für den Command hast du nicht genügend Rechte";

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
