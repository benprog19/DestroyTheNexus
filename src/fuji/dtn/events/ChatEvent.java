package fuji.dtn.events;

import fuji.dtn.game.GameState;
import fuji.dtn.kits.Kits;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

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

        if (GameState.getGameState().equals(GameState.INGAME)) {
            if (team != null) {
                if (!e.getMessage().startsWith("@")) {
                    for (UUID pls : team.getPlayers()) {
                        Player player1 = Bukkit.getPlayer(pls);
                        player1.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "TEAM " + ChatColor.DARK_GRAY + "(" + team.getColor() + kitname + ChatColor.DARK_GRAY + ") " + team.getColor() + player.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + e.getMessage().replaceFirst("@", ""));
                        player1.playSound(player1.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
                        e.setCancelled(true);
                    }
                } else {
                    e.setFormat(ChatColor.DARK_GRAY + "[" + team.getColor() + team.getName() + ChatColor.DARK_GRAY + "] (" + team.getColor() + kitname + ChatColor.DARK_GRAY + ") " + team.getColor() + player.getName() + ChatColor.DARK_GRAY + ": " + team.getColor() + e.getMessage());
                }
            } else {
                e.setFormat(ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + kitname + ChatColor.DARK_GRAY + ") " + ChatColor.GREEN + player.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + e.getMessage());
            }
        } else {
            e.setFormat(ChatColor.GREEN + player.getName() + ": " + ChatColor.WHITE + e.getMessage());
        }
    }

}
