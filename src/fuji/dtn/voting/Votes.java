package fuji.dtn.voting;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/18/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Votes {

    static ConcurrentHashMap<UUID, Vote> votes = new ConcurrentHashMap<>();

    public static boolean userHasVoted(UUID user) {
        if (votes.containsKey(user)) {
            return true;
        }
        return false;
    }

    public static Vote getVoteFromUser(UUID user) {
        return votes.get(user);
    }

    public static void addNewVote(UUID user, Vote vote) {
        if (!userHasVoted(user)) {
            votes.put(user, vote);
            vote.addVote(user);
        } else {
            // Remove old vote
            Vote vote1 = getVoteFromUser(user);
            votes.remove(user);
            vote1.removeVote(user);

            // Add new vote
            votes.put(user, vote);
            vote.addVote(user);
        }
    }

    public static Vote getHighestVote() {
        ConcurrentHashMap<Vote, Integer> voteCount = new ConcurrentHashMap<>();
        for ( Map.Entry<UUID, Vote> entry : votes.entrySet()) {
            Vote vote = entry.getValue();
            Integer count = voteCount.get(vote);
            if (count != null) {
                voteCount.put(vote, count+1);
            } else {
                voteCount.put(vote, 1);
            }
        }

        int maxValueInMap = (Collections.max(voteCount.values()));  // This will return max value in the Hashmap
        for (Map.Entry<Vote, Integer> entry : voteCount.entrySet()) {  // Itrate through hashmap
            if (entry.getValue() == maxValueInMap) {
                return entry.getKey();   // Print the key with max value
            }
        }
        return null;
    }
}
