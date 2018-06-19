package fuji.dtn.voting;

import fuji.dtn.arena.Arena;

import java.util.ArrayList;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/15/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Vote {

    int votes;
    Arena arena;

    ArrayList<UUID> usersVote = new ArrayList<>();

    public Vote(Arena arena) {
        this.arena = arena;
    }

    // do not use this method by itself, use the Votes class.
    public void addVote(UUID uuid) {
        usersVote.add(uuid);
        votes = usersVote.size();
    }

    public int getVotes() {
        return votes;
    }

    // do not use this method by itself, use the Votes class.
    public void removeVote(UUID uuid) {
        usersVote.remove(uuid);
        votes = usersVote.size();
    }

    public Arena getArena() {
        return arena;
    }
}
