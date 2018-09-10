package fuji.dtn.commands;

import fuji.dtn.game.GameState;
import fuji.dtn.voting.Vote;
import fuji.dtn.voting.Votes;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/20/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class VoteCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vote")) {
            Player player = (Player) sender;
            if (GameState.getGameState().equals(GameState.WAITING)) {
                if (args.length == 0) {
                    ArrayList<Vote> votes = Votes.getAvailableVotes();
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "  Available Votes:");
                    for (int i = 0; i < votes.size(); i++) {
                        player.sendMessage(ChatColor.DARK_GRAY + "    " + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + (i + 1) + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "] " + ChatColor.BLUE + votes.get(i).getArena().getName());
                    }
                    player.sendMessage("");
                    player.sendMessage(ChatColor.RED + "     Use /vote <num> to vote for your map.");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("end")) {
                        Votes.endVote();
                    } else {
                        int voteNum = Integer.parseInt(args[0]);
                        int num = voteNum - 1;
                        Vote vote = Votes.getAvailableVotes().get(num);
                        Votes.addNewVote(player.getUniqueId(), vote);
                        player.sendMessage(ChatColor.GOLD + "You voted for " + ChatColor.RED + vote.getArena().getName() + ChatColor.GOLD
                                + ".  Your vote now has " + ChatColor.RED + vote.getVotes() + ChatColor.GOLD + " votes.");
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "You can only use this during voting time.");
            }
        }
        return true;
    }
}
