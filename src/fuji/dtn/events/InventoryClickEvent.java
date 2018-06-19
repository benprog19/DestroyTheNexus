package fuji.dtn.events;

import fuji.dtn.game.GameState;
import fuji.dtn.kits.Kit;
import fuji.dtn.kits.Kits;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
        final Player player = (Player) e.getWhoClicked();
        final ItemStack itemStack = e.getCurrentItem();
        if (GameState.getGameState().equals(GameState.WAITING)) {
            if (!player.isOp()) {
                e.setCancelled(true);
            }

            if (e.getInventory().getName().equalsIgnoreCase(ChatColor.BOLD + "Kit Selector")) {
                if (itemStack.getType().equals(Material.BANNER)) {
                    Kit kit = InteractEvent.storedSlotToKit.get(e.getSlot());
                    if (kit != null) {
                        Kits.addPlayerToKit(player, kit);
                        player.closeInventory();
                    } else {
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "That kit does not exist.");
                    }
                }
            }
        }

    }


}
