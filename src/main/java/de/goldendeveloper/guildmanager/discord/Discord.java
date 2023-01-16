package de.goldendeveloper.guildmanager.discord;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import de.goldendeveloper.guildmanager.Main;
import de.goldendeveloper.guildmanager.discord.events.Events;
import de.goldendeveloper.guildmanager.discord.events.RegisterCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Date;

public class Discord {

    public static JDA bot;

    public Discord(String BOT_TOKEN) {
        try {
            bot = JDABuilder.createDefault(BOT_TOKEN)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.ROLE_TAGS, CacheFlag.STICKER, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
                    .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                            GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES,
                            GatewayIntent.GUILD_BANS, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGE_TYPING,
                            GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGE_TYPING)
                    .addEventListeners(new RegisterCommands(), new Events())
                    .setAutoReconnect(true)
                    .setContextEnabled(true)
                    .build().awaitReady();
            registerCommand();
            if (Main.getDeployment()) {
                Online();
                Main.getServerCommunicator().startBot(bot);
            }
            bot.getPresence().setActivity(Activity.playing("/help | " + bot.getGuilds().size() + " Servern"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void registerCommand() {
        bot.upsertCommand(RegisterCommands.Ping, "Antwortet mit Pong").queue();
        bot.upsertCommand(RegisterCommands.CmdShutdown, "Stoppt den Discord Bot!").queue();
        bot.upsertCommand(RegisterCommands.BotStats, "Zeigt dir die Stats des Bots").queue();
        bot.upsertCommand(RegisterCommands.Server_Owner, "Nennt den Server Inhaber").queue();
        bot.upsertCommand(RegisterCommands.CmdRestart, "Startet den Discord Bot neu!").queue();
        bot.upsertCommand(RegisterCommands.Donate, "Zeigt dir eine Spende möglichkeit").queue();
        bot.upsertCommand(RegisterCommands.Donate, "Zeigt dir eine Spende möglichkeit").queue();
        bot.upsertCommand(RegisterCommands.Bot_Owner, "Zeigt dir den Bot Programmierer").queue();
        bot.upsertCommand(RegisterCommands.ServerStats, "Zeigt dir die Stats des Servers").queue();
        bot.upsertCommand(RegisterCommands.Help, "Zeigt eine Liste von möglichen Befehlen").queue();
        bot.upsertCommand(RegisterCommands.Invite, "Du wirst eingeladen auf unseren Discord").queue();
        bot.upsertCommand(RegisterCommands.Join_Channel, "Der Bot betritt deinen Voice Channel").queue();
        bot.upsertCommand(RegisterCommands.Leave_Channel, "Der Bot verlässt deinen Voice Channel").queue();
        bot.upsertCommand(RegisterCommands.Join, "Der Bot sendet dir einen Link um ihn einzuladen").queue();
        bot.upsertCommand(RegisterCommands.Error_report, "Reporte einen Bot fehler").addOption(OptionType.STRING, "error", "Schildere hier deinen gefundenen Bot Fehler", true).queue();
        bot.upsertCommand(RegisterCommands.Birthday, "Gratuliere einem anderen User").addOption(OptionType.USER, "user", "Fügt einen anderen User hinzu", true).addOption(OptionType.BOOLEAN, "private", "Möchtest du dem User die Glückwünsche privat zukommen lassen?").queue();
        bot.upsertCommand(RegisterCommands.Ban, "Bannt einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.INTEGER, "time", "Gib die Ban Dauer in Tagen an um den User zu bannen", true, true).addOption(OptionType.STRING, "reason", "Begründe deinen Ban", true).queue();
        bot.upsertCommand(RegisterCommands.Kick, "Kickt einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.STRING, "reason", "Begründe deinen Kick").queue();
        bot.upsertCommand(RegisterCommands.TimeOut, "Timeoute einen bestimmten Spieler").addOption(OptionType.USER, "user", "Füge einen Benutzer hinzu", true).addOption(OptionType.STRING, "time", "Gib die Timeout Dauer in Tagen an um den User zu timeouten. (In Minuten)", true).queue();
        bot.upsertCommand(RegisterCommands.Clear, "Löscht eine Anzahl von Nachrichten!").addOption(OptionType.INTEGER, RegisterCommands.ClearOptionAmount, "Anzahl von löschenden Nachrichten!", true).queue();

        bot.upsertCommand(RegisterCommands.settings, "Stelle den GuildManager ein!")
                .addSubcommands(
                        new SubcommandData(RegisterCommands.settingsSupJoinRole, "Die Rolle die einem User automatisch beim Joinen gegeben werden soll!").addOption(OptionType.ROLE, RegisterCommands.settingsSupJoinRoleOptionRole, "Join Rolle", true),
                        new SubcommandData(RegisterCommands.settingsSupRemove, "Zeigt die eine Liste aller Option die Entfernt werden können!"),
                        new SubcommandData(RegisterCommands.settingsSupWMessage, "Setzte einen Willkommens Channel um deine User willkommen zu heißen!").addOption(OptionType.CHANNEL, RegisterCommands.settingsSupWMessageOptionChannel, "Willkommens Channel", true)
                ).queue();
    }

    public void sendErrorMessage(String Error) {
        WebhookEmbedBuilder embed = new WebhookEmbedBuilder();
        embed.setAuthor(new WebhookEmbed.EmbedAuthor(getBot().getSelfUser().getName(), getBot().getSelfUser().getAvatarUrl(), "https://Golden-Developer.de"));
        embed.addField(new WebhookEmbed.EmbedField(false, "[ERROR]", Error));
        embed.setColor(0xFF0000);
        embed.setFooter(new WebhookEmbed.EmbedFooter("@Golden-Developer", getBot().getSelfUser().getAvatarUrl()));
        new WebhookClientBuilder(Main.getConfig().getDiscordWebhook()).build().send(embed.build());
    }

    public JDA getBot() {
        return bot;
    }

    private void Online() {
        WebhookEmbedBuilder embed = new WebhookEmbedBuilder();
        if (Main.getRestart()) {
            embed.setColor(0x33FFFF);
            embed.addField(new WebhookEmbed.EmbedField(false, "[Status]", "Neustart erfolgreich"));
        } else {
            embed.setColor(0x00FF00);
            embed.addField(new WebhookEmbed.EmbedField(false, "[Status]", "ONLINE"));
        }
        embed.setAuthor(new WebhookEmbed.EmbedAuthor(getBot().getSelfUser().getName(), getBot().getSelfUser().getAvatarUrl(), "https://Golden-Developer.de"));
        embed.addField(new WebhookEmbed.EmbedField(false, "Gestartet als", bot.getSelfUser().getName()));
        embed.addField(new WebhookEmbed.EmbedField(false, "Server", Integer.toString(bot.getGuilds().size())));
        embed.addField(new WebhookEmbed.EmbedField(false, "Status", "\uD83D\uDFE2 Gestartet"));
        embed.addField(new WebhookEmbed.EmbedField(false, "Version", Main.getConfig().getProjektVersion()));
        embed.setFooter(new WebhookEmbed.EmbedFooter("@Golden-Developer", getBot().getSelfUser().getAvatarUrl()));
        embed.setTimestamp(new Date().toInstant());
        new WebhookClientBuilder(Main.getConfig().getDiscordWebhook()).build().send(embed.build());
    }
}
