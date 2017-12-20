package fuji.dtn.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 11/29/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class InventoryClickEvent implements Listener {

    @EventHandler
    public void InventoryClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        //final Player player = (Player) e.getWhoClicked();
        final ItemStack itemStack = e.getCurrentItem();
        if (itemStack != null) {
            // Game Option
        }

    }


}
