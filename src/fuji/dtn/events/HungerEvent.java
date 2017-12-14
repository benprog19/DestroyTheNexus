package fuji.dtn.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 12/13/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class HungerEvent implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        final Player player = (Player) e.getEntity();
        e.setCancelled(true);
        player.setFoodLevel(20);
        e.setFoodLevel(20);
    }

}
