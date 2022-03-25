package de.goldendeveloper.gdguildmanager.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.HashSet;
import java.util.Set;

public class AudioInfo {

    private final AudioTrack track;
    private final Set<String> skips;
    private final Member author;

    AudioInfo(AudioTrack track, Member author) {
        this.track = track;
        this.skips = new HashSet<>();
        this.author = author;
    }

    public AudioTrack getTrack() {
        return track;
    }

    public int getSkips() {
        return skips.size();
    }

    public void addSkip(User u) {
        skips.add(u.getId());
    }

    public boolean hasVoted(User u) {
        return skips.contains(u.getId());
    }

    public Member getAuthor() {
        return author;
    }

}
