package fuji.dtn.events;

import fuji.dtn.kits.Kits;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 9/5/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();
        Team team = Teams.getTeamFromPlayer(player);
        String kitname = Kits.getKitNameByPlayer(player);

        if (team != null) {
            e.setFormat(ChatColor.DARK_GRAY + "[" + team.getColor() + team.getName() + ChatColor.DARK_GRAY + "] (" + team.getColor() + kitname + ChatColor.DARK_GRAY + ") " + team.getColor() + player.getName() + ChatColor.DARK_GRAY + ": " + team.getColor() + e.getMessage());
        } else {
            e.setFormat(ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + kitname + ChatColor.DARK_GRAY + ") " + ChatColor.GREEN + player.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + e.getMessage());
        }
    }

}
