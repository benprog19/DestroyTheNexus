package fuji.dtn.events;

import fuji.dtn.arena.Arena;
import fuji.dtn.game.GameState;
import fuji.dtn.game.Players;
import fuji.dtn.game.Spectators;
import fuji.dtn.rotation.Rotation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 12/18/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class MoveEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        Arena arena = Rotation.getCurrentArena();

        if (GameState.getGameState().equals(GameState.INGAME)) {
            if (Spectators.isSpectator(player) || DeathEvent.deadPlayers.contains(player.getUniqueId())) {
                if (player.getLocation().getY() < 0) {
                    if (arena != null) {
                        player.teleport(arena.getRedLocation());
                    } else {
                        player.teleport(new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, 64.0, player.getLocation().getBlockZ() + 0.5));
                    }
                }
            } else if (Players.isPlayer(player)) {
                if (player.getLocation().getY() < 0) {
                    if (arena != null) {
                        player.setHealth(0);
                        player.teleport(arena.getRedLocation());
                    } else {
                        player.teleport(new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, 64.0, player.getLocation().getBlockZ() + 0.5));
                    }
                }
            }
        }
    }
}
