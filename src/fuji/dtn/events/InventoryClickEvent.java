package fuji.dtn.events;

import fuji.dtn.arena.Arena;
import fuji.dtn.game.GameState;
import fuji.dtn.kits.Kit;
import fuji.dtn.kits.Kits;
import fuji.dtn.voting.Votes;
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
        if (GameState.getGameState().equals(GameState.WAITING) || GameState.getGameState().equals(GameState.STARTING)) {
            if (!player.isOp()) {
                e.setCancelled(true);
            }

            if (e.getInventory().getName().equalsIgnoreCase(ChatColor.BOLD + "Kit Selector")) {
                if (itemStack.getType().equals(Material.BANNER)) {
                    e.setCancelled(true);
                    Kit kit = InteractEvent.storedSlotToKit.get(e.getSlot());
                    if (kit != null) {
                        Kits.addPlayerToKit(player, kit);
                        player.closeInventory();
                    } else {
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "That kit does not exist.");
                    }
                } else {
                    e.setCancelled(true);
                }
            } else if (e.getInventory().getName().equalsIgnoreCase(ChatColor.BOLD + "Map Voting")) {
                if (itemStack.getType().equals(Material.EMPTY_MAP)) {
                    e.setCancelled(true);
                    Arena arena = InteractEvent.storedSlotToArena.get(e.getSlot());
                    if (arena != null) {
                        Votes.addNewVote(player.getUniqueId(), Votes.getAvailableVotes().get(e.getSlot()));
                        player.sendMessage(ChatColor.GOLD + "You voted for " + ChatColor.RED + arena.getName() + ChatColor.GOLD
                                + ".  Your vote now has " + ChatColor.RED + Votes.getAvailableVotes().get(e.getSlot()).getVotes() + ChatColor.GOLD + " votes.");
                    } else {
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "That vote does not exist.");
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }

    }


}
