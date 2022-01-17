package de._Coho04_.DiscordBot.Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class Commands {


    private static boolean hasPlayer(Guild guild) {
        return players.containsKey(guild.getId());
    }

    private static final int PLAYLIST_LIMIT = 200;
    private static final Map<String, Map.Entry<AudioPlayer, TrackManager>> players = new HashMap<>();
    private static final AudioPlayerManager myManager = new DefaultAudioPlayerManager();

    private static final String CD = "\uD83D\uDCBF";
    private static final String DVD = "\uD83D\uDCC0";
    private static final String MIC = "\uD83C\uDFA4 **|>** ";

    private static final String QUEUE_DESCRIPTION = "%s **|>**  %s\n%s\n%s %s\n%s";
    private static final String QUEUE_INFO = "Info about the Queue: (Size - %d)";
    private static final String ERROR = "Error while loading \"%s\"";

    public static void onSkip(SlashCommandEvent e) {
        if (isIdle(e.getChannel(), e.getGuild())) return;

        if (isCurrentDj(e.getMember())) {
            forceSkipTrack(e.getGuild(), e.getChannel());
        }
    }

    public static void onReset(SlashCommandEvent e) {
        e.getInteraction().reply("You don't have the required permissions to do that! [DJ role]").queue();

    }

    public static void onShuffle(SlashCommandEvent e) {
        if (isIdle(e.getChannel(), e.getGuild())) return;
        getTrackManager(e.getGuild()).shuffleQueue();
        e.getInteraction().reply("\u2705 Shuffled the queue!").queue();
    }

    private static boolean isIdle(MessageChannel chat, Guild guild) {
        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
            chat.sendMessage("No music is being played at the moment!").queue();
            return true;
        }
        return false;
    }

    public static void onForceSkip(SlashCommandEvent e) {
        if (isIdle(e.getChannel(), e.getGuild())) return;
        forceSkipTrack(e.getGuild(), e.getChannel());
    }

    public static void onQueue(SlashCommandEvent e) {
        if (!hasPlayer(e.getGuild()) || getTrackManager(e.getGuild()).getQueuedTracks().isEmpty()) {
            e.getInteraction().reply("The queue is empty! Load a song with ** /play <Song> **!").queue();
        } else {
            StringBuilder sb = new StringBuilder();
            Set<AudioInfo> queue = getTrackManager(e.getGuild()).getQueuedTracks();
            queue.forEach(audioInfo -> sb.append(buildQueueMessage(audioInfo)));
            String embedTitle = String.format(QUEUE_INFO, queue.size());
            sb.setLength(sb.length() - 1);
        }
    }

    public static void onInfo(SlashCommandEvent e) {
        if (!hasPlayer(e.getGuild()) || getPlayer(e.getGuild()).getPlayingTrack() == null) { // No song is playing
            e.reply("No song is being played at the moment! *It's your time to shine..*").queue();
        } else {
            AudioTrack track = getPlayer(e.getGuild()).getPlayingTrack();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.addField("Track Info", String.format(QUEUE_DESCRIPTION, CD, getOrNull(track.getInfo().title) +
                    "\n\u23F1 **|>** `[ " + getTimestamp(track.getPosition()) + " / " + getTimestamp(track.getInfo().length) + " ]`" +
                    "\n" + MIC + getOrNull(track.getInfo().author) +
                    "\n\uD83C\uDFA7 **|>**  "), false);
            e.getInteraction().replyEmbeds(embedBuilder.build()).queue();
        }
    }


    private static void reset(Guild guild) {
        players.remove(guild.getId());
        getPlayer(guild).destroy();
        getTrackManager(guild).purgeQueue();
        guild.getAudioManager().closeAudioConnection();
    }


    public void Command() {
        AudioSourceManagers.registerRemoteSources(myManager);
    }

    public void executeCommand(String[] args, MessageReceivedEvent e, MessageChannel chat) {
        switch (args.length) {
            case 0: // Show help message
                break;
            case 1:
                switch (args[0].toLowerCase()) {
                    case "help":
                    case "commands":
                        break;
                    case "now":
                    case "current":
                    case "nowplaying":
                }
            default:
                String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                switch (args[0].toLowerCase()) {
                    case "ytplay": // Query YouTube for a music video
                        input = "ytsearch: " + input;
                        // no break;
                    case "play": // Play a track
                        if (args.length <= 1) {
                            chat.sendMessage("Please include a valid source.").queue();
                        } else {
                            loadTrack(input, e.getMember(), e.getMessage(), chat);
                        }
                        break;
                }
                break;
        }
    }

    public List<String> getAlias() {
        return Collections.singletonList("music");
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (!players.containsKey(event.getGuild().getId()))
            return;
        TrackManager manager = getTrackManager(event.getGuild());
        manager.getQueuedTracks().stream().filter(info -> !info.getTrack().equals(getPlayer(event.getGuild()).getPlayingTrack()) && info.getAuthor().getUser().equals(event.getMember().getUser())).forEach(manager::remove);
    }

    public void onGuildLeave(GuildLeaveEvent event) {
        reset(event.getGuild());
    }

    private void tryToDelete(Message m) {
        if (m.getGuild().getSelfMember().hasPermission(m.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            m.delete().queue();
        }
    }


    private static TrackManager getTrackManager(Guild guild) {
        return players.get(guild.getId()).getValue();
    }


    private void loadTrack(String identifier, Member author, Message msg, MessageChannel chat) {
        if (author.getVoiceState().getChannel() == null) {
            chat.sendMessage("You are not in a Voice Channel!").queue();
            return;
        }

        Guild guild = author.getGuild();
        getPlayer(guild);

        msg.getTextChannel().sendTyping().queue();
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getTrackManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.addField("TEST ", String.format(QUEUE_DESCRIPTION, DVD, getOrNull(playlist.getName()), "", "", "", ""), true);
                    chat.sendMessageEmbeds(embedBuilder.build()).queue();
                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setDescription(String.format(ERROR, identifier) + "\u26A0 No playable tracks were found.");
                chat.sendMessageEmbeds(embedBuilder.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setDescription(String.format(ERROR, identifier) + "\u26D4 " + exception.getLocalizedMessage());
                chat.sendMessageEmbeds(embedBuilder.build()).queue();
            }
        });
        tryToDelete(msg);
    }


    private static boolean isCurrentDj(Member member) {
        return getTrackManager(member.getGuild()).getTrackInfo(getPlayer(member.getGuild()).getPlayingTrack()).getAuthor().equals(member);
    }


    private static void forceSkipTrack(Guild guild, MessageChannel chat) {
        getPlayer(guild).stopTrack();
        chat.sendMessage("\u23E9 Skipping track!").queue();
    }


    private static String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    private static String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    private static String getOrNull(String s) {
        return s.isEmpty() ? "N/A" : s;
    }

    private static AudioPlayer getPlayer(Guild guild) {
        AudioPlayer p;
        if (hasPlayer(guild)) {
            p = players.get(guild.getId()).getKey();
        } else {
            AudioPlayer nPlayer = myManager.createPlayer();
            TrackManager manager = new TrackManager(nPlayer);
            nPlayer.addListener(manager);
            guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(nPlayer));
            players.put(guild.getId(), new AbstractMap.SimpleEntry<>(nPlayer, manager));
            p = nPlayer;
        }
        return p;
    }
}
