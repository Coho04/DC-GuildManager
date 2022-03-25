package de.goldendeveloper.gdguildmanager;

import de.goldendeveloper.gdguildmanager.events.RegisterCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDA Bot;

    public static void main(String[] args) {
        BotCreate();
    }

    public static void BotCreate() {
        try {
            String BOT_TOKEN = "OTMxMTQxMzU5MTcwMTA5NDUx.YeAG9w.fRlgo2B9Luufjg2a0jfS3K27GkU";
            JDABuilder BBot = JDABuilder.createDefault(BOT_TOKEN);
            BBot.setChunkingFilter(ChunkingFilter.ALL);
            BBot.setMemberCachePolicy(MemberCachePolicy.ALL);
            BBot.enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.ROLE_TAGS, CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS);
            BBot.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS,
                    GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES,
                    GatewayIntent.GUILD_BANS, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGE_TYPING,
                    GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGE_TYPING);
            BBot.addEventListeners(new RegisterCommands());
            BBot.setAutoReconnect(true);
            Bot = BBot.build();
            RegisterCommands.registerCommand();
        } catch (LoginException e) {
            e.printStackTrace();
        }

/*        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
//        playerManager.
                AudioPlayer player = playerManager.createPlayer();
        TrackScheduler trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);*/
    }
}