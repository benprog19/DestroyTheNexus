package fuji.dtn.events;

import fuji.dtn.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 5/29/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class ItemPickUpEvent implements Listener {

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (GameState.getGameState().equals(GameState.WAITING)) {
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
        }
    }
}
