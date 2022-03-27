package de.goldendeveloper.guildmanager;

import de.goldendeveloper.guildmanager.events.RegisterCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Discord {

    public static JDA bot;

    public Discord(String BOT_TOKEN) {
        try {
            bot = JDABuilder.createDefault(BOT_TOKEN)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.ROLE_TAGS, CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
                    .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS,
                            GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES,
                            GatewayIntent.GUILD_BANS, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGE_TYPING,
                            GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGE_TYPING)
                    .addEventListeners(new RegisterCommands())
                    .setAutoReconnect(true)
                    .build().awaitReady();
            registerCommand();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void registerCommand() {
        bot.upsertCommand(RegisterCommands.CMD_Leave_s, "Lässt den Bot den Server verlassen").queue();
        bot.upsertCommand(RegisterCommands.CMD_Server_Owner_s, "Nennt den Server Inhaber").queue();
        bot.upsertCommand(RegisterCommands.CMD_Join_s, "Der Bot sendet dir einen link um ihn einzuladen").queue();
        bot.upsertCommand(RegisterCommands.CMD_Invite_s, "Du wirst eingeladen auf unseren Discord").queue();
        bot.upsertCommand(RegisterCommands.CMD_Join_Channel_s, "Der Bot betritt deinen Voice Channel").queue();
        bot.upsertCommand(RegisterCommands.CMD_Leave_Channel_s, "Der Bot verlässt deinen Voice Channel").queue();
        bot.upsertCommand(RegisterCommands.CMD_Help_s, "Zeigt eine Liste von möglichen Befehlen").queue();
        bot.upsertCommand(RegisterCommands.CMD_Birthday_s, "Gratuliere einem anderen User").addOption(OptionType.USER, "user", "Fügt einen anderen User hinzu", true).addOption(OptionType.BOOLEAN, "private", "Möchtest du dem User die Glückwünsche privat zukommen lassen?").queue();
        bot.upsertCommand(RegisterCommands.CMD_Bot_Owner_s, "Zeigt dir den Bot Programmierer").queue();
        bot.upsertCommand(RegisterCommands.CMD_Error_report_s, "Reporte einen Bot fehler").addOption(OptionType.STRING, "error", "Schildere hier deinen gefundenen Bot Fehler", true).queue();
        bot.upsertCommand(RegisterCommands.CMD_Ping_s, "Antwortet mit Pong").queue();
        bot.upsertCommand(RegisterCommands.CMD_Donate_s, "Zeigt dir eine Spende möglichkeit").queue();
        bot.upsertCommand(RegisterCommands.CMD_Server_Stats_s, "Zeigt dir die Stats des Servers").queue();
        bot.upsertCommand(RegisterCommands.CMD_Bot_Stats_s, "Zeigt dir die Stats des Bots").queue();
        bot.upsertCommand(RegisterCommands.CMD_Donate_s, "Zeigt dir eine Spende möglichkeit").queue();
        bot.upsertCommand(RegisterCommands.CMD_Ban_s, "Bannt einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.INTEGER, "time", "Gib die Ban Dauer in Tagen an um den User zu bannen", true).addOption(OptionType.STRING, "reason", "Begründe deinen Ban", true).queue();
        bot.upsertCommand(RegisterCommands.CMD_Kick_s, "Kickt einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.STRING, "reason", "Begründe deinen Kick", true).queue();
        bot.upsertCommand(RegisterCommands.CMD_TimeOut_s, "Timeoutet einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.STRING, "time", "Gib die Timeout Dauer in Tagen an um den User zu timeouten. (In Minuten)", true).queue();
        bot.upsertCommand(RegisterCommands.CMD_Stop_s, "Stoppt den Bot").queue();
        bot.upsertCommand(RegisterCommands.CMD_Activity_s, "Ändert die Aktivität des Bots").addOption(OptionType.STRING, "type", "playing watching competing streaming listening custom", true).addOption(OptionType.STRING, "activity", "Hier kommt die Aktivität hin", true).addOption(OptionType.STRING, "url", "Füge deiner Option eine URL hinzu").queue();

    }

    public JDA getBot() {
        return bot;
    }
}
