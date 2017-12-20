package fuji.dtn.events;

import fuji.dtn.game.GameState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 12/15/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class DamageEvent implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        final Entity entity = e.getEntity();

        if (entity instanceof Player) {
            if (GameState.getGameState().equals(GameState.INGAME)) {
                e.setCancelled(false);
            } else{
                e.setCancelled(true);
            }
        }
    }
}
