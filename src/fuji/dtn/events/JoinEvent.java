package fuji.dtn.events;

import fuji.dtn.game.GameState;
import fuji.dtn.game.Players;
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
        }
    }
}
