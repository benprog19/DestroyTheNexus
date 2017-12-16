package fuji.dtn.events;

import fuji.dtn.game.*;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
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
                Game.tryStart();
            } else if (GameState.getGameState().equals(GameState.STARTING)) {
                Team red = Teams.getTeamByName("red");
                Team blue = Teams.getTeamByName("blue");

                if (red.getPlayers().size() > blue.getPlayers().size()) {
                    blue.addPlayer(player);
                    player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "You have joined the BLUE Team.");
                } else if (blue.getPlayers().size() > red.getPlayers().size()) {
                    red.addPlayer(player);
                    player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You have joined the RED Team.");
                } else if (red.getPlayers().size() == blue.getPlayers().size()) {
                    red.addPlayer(player);
                    player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You have joined the RED Team.");
                }
            }

        } else {
            player.sendMessage(ChatColor.GOLD + "You have joined the " + ChatColor.YELLOW + "" + ChatColor.BOLD + "SPECTATORS.");
            Spectators.addPlayer(player);
            player.teleport(Rotation.getCurrentArena().getRedLocation());
        }
    }


}
