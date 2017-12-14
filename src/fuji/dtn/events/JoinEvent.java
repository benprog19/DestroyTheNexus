package fuji.dtn.events;

import fuji.dtn.game.*;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 11/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class JoinEvent implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (GameState.getGameState() == GameState.WAITING || GameState.getGameState() == GameState.STARTING) {
            Players.addPlayer(player);
            if (Lobby.getLobbyLoc() != null) {
                player.teleport(Lobby.getLobbyLoc());
            } else {
                player.sendMessage(ChatColor.RED + "Lobby has not been set yet. You have been teleported to your last known location.");
            }

            if (GameState.getGameState().equals(GameState.WAITING)) {
                if (Bukkit.getOnlinePlayers().size() >= 2) {
                    Players.getPlayers().clear();

                    Team red = Teams.getTeamByName("red");
                    Team blue = Teams.getTeamByName("blue");

                    red.getPlayers().clear();
                    blue.getPlayers().clear();

                    for (Player pls : Bukkit.getOnlinePlayers()) {
                        Players.addPlayer(pls);
                        pls.sendMessage(ChatColor.GREEN + "You have been added into the game.");
                    }

                    try {
                        new Rotation();
                        Game game = new Game(Rotation.getCurrentArena(), Players.getPlayers());
                        game.beginGame();
                    } catch (IllegalStateException ex) {
                        Bukkit.broadcastMessage(ChatColor.RED + "There are no available games to play.");
                    }


                } else {
                    player.sendMessage(ChatColor.RED + "You must wait until more players join before the game begins.");
                }
            }

        } else {
            player.sendMessage(ChatColor.GOLD + "You have joined the " + ChatColor.YELLOW + "" + ChatColor.BOLD + "SPECTATORS.");
            Spectators.addPlayer(player);
            player.teleport(Rotation.getCurrentArena().getRedLocation());
        }
    }
}
