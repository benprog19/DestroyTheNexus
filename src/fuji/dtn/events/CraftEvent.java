package fuji.dtn.events;

import fuji.dtn.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/11/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class CraftEvent implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (GameState.getGameState().equals(GameState.INGAME)) {
            for (int i = 0; i < e.getInventory().getViewers().size(); i++) {
                e.getInventory().getViewers().get(i).closeInventory();
            }
            e.setCancelled(true);
        }
    }
}
