package fuji.dtn.events;

import fuji.dtn.game.Game;
import fuji.dtn.game.GameState;
import fuji.dtn.game.Players;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 11/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class QuitEvent implements Listener {


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (Players.isPlayer(player)) {
            Players.removePlayer(player);
        }
        Team playerTeam = Teams.getTeamFromPlayer(player);

        if (playerTeam != null) {
            playerTeam.removePlayer(player);
            e.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "] " + playerTeam.getColor() + player.getName());
        } else {
            e.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName());
        }

        if (GameState.getGameState().equals(GameState.INGAME)) {
            int size = Bukkit.getOnlinePlayers().size() - 1;
            System.out.print("Size: " + size);
            if (size <= 1) {
                Bukkit.reload();
            } else {
                int playersSize = Players.getPlayers().size();
                if (playersSize <= 1) {
                    Bukkit.reload();
                } else {
                    Team red = Teams.getTeamByName("red");
                    Team blue = Teams.getTeamByName("blue");
                    if (red.getPlayers().size() > 0 && blue.getPlayers().size() == 0) {
                        Game.endGame(red);
                    } else if (blue.getPlayers().size() > 0 && red.getPlayers().size() == 0) {
                        Game.endGame(blue);
                    }
                }
            }
        }
    }

}
