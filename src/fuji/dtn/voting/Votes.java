package fuji.dtn.voting;

import fuji.dtn.arena.Arena;
import fuji.dtn.arena.Arenas;
import fuji.dtn.game.Game;
import fuji.dtn.rotation.Rotation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/18/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Votes {

    static ArrayList<Vote> allowedVotes = new ArrayList<>();
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
        if (allowedVotes.contains(vote)) {
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
    }

    public static void endVote() {
        Vote vote = Votes.getHighestVote();
        Bukkit.broadcastMessage(ChatColor.GOLD + "The vote has been closed and the most voted for map is " + ChatColor.RED + vote.getArena().getName() + ChatColor.GOLD
                + ".");
        Rotation.setNewArena(vote.getArena().getName());
        ArrayList<UUID> players = new ArrayList<>();
        for (Player pls : Bukkit.getOnlinePlayers()) {
            players.add(pls.getUniqueId());
        }
        Game.init(vote.getArena(), players);
    }

    public static Vote getHighestVote() {
//        ConcurrentHashMap<Vote, Integer> voteCount = new ConcurrentHashMap<>();
//        for ( Map.Entry<UUID, Vote> entry : votes.entrySet()) {
//            Vote vote = entry.getValue();
//            Integer count = voteCount.get(vote);
//            if (count != null) {
//                voteCount.put(vote, count+1);
//            } else {
//                voteCount.put(vote, 1);
//            }
//        }
//
//        int maxValueInMap = (Collections.max(voteCount.values()));  // This will return max value in the Hashmap
//        for (Map.Entry<Vote, Integer> entry : voteCount.entrySet()) {  // Itrate through hashmap
//            if (entry.getValue() == maxValueInMap) {
//                return entry.getKey();   // Print the key with max value
//            }
//        }
//        return null;

        Vote highest = null;
        if (!allowedVotes.isEmpty()) {
            for (int i = 0; i < allowedVotes.size(); i++) {
                Vote vote = allowedVotes.get(i);
                if (highest == null) {
                    highest = vote;
                } else if (highest.getVotes() <= vote.getVotes()) {
                    highest = vote;
                }
            }
        } else {
            ArrayList<Arena> playableArenas = new ArrayList<>();
            ArrayList<Arena> arenas = Arenas.getRegisteredArenas();

            for (int i = 0; i < arenas.size(); i++) {
                if (Bukkit.getOnlinePlayers().size() >= arenas.get(i).getMinPlayers()) {
                    playableArenas.add(arenas.get(i));
                }
            }

            int max = playableArenas.size();
            int rand = new Random().nextInt(max);
            Arena arena = playableArenas.get(rand);
            if (arena.isPlayable()) {
                highest = new Vote(arena);
            }
        }
        return highest;
    }

    public static void startVoting() {
        allowedVotes.clear();
        ArrayList<Arena> arenas =  Arenas.getRegisteredArenas();
        ArrayList<String> playableArenas = new ArrayList<>();

        for (int i = 0; i < arenas.size(); i++) {
            if (Bukkit.getOnlinePlayers().size() >= arenas.get(i).getMinPlayers()) {
                playableArenas.add(arenas.get(i).getName());
            }
        }

        while (allowedVotes.size() < 5) {
            if (allowedVotes.size() > 0) {
                for (int i = 0; i < allowedVotes.size(); i++) {
                    Vote vote = allowedVotes.get(i);
                    playableArenas.remove(vote.getArena().getName());
                }
            }
            int max = playableArenas.size();
            System.out.print(max);
            int rand = new Random().nextInt(max);
            Arena arena = Arenas.getArenaByName(playableArenas.get(rand));
            System.out.print("AllowedVotes: " + playableArenas.toString());
            if (arena.isPlayable()) {
                allowedVotes.add(new Vote(arena));
                System.out.print("+ " + arena.getName());

            }
        }

        for (int i = 0; i < allowedVotes.size(); i++) {
            System.out.print("Vote: " + allowedVotes.get(i).getArena().getName());
        }
    }

    public static ArrayList<Vote> getAvailableVotes() {
        return allowedVotes;
    }
}
