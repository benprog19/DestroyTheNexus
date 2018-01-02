package fuji.dtn.events;

import fuji.dtn.game.GameState;
import fuji.dtn.kits.Kits;
import fuji.dtn.menus.Menu;
import fuji.dtn.menus.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 1/1/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (DeathEvent.isDead(player)) {
            e.setCancelled(true);
        } else {
            if (GameState.getGameState().equals(GameState.WAITING) || GameState.getGameState().equals(GameState.STARTING)) {
                if (player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
                    if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Kit Selector " + ChatColor.GRAY + "(Right-Click)")) {
                        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            int menuSize;
                            int size = Kits.getAllRegisteredKits().size();
                            if (size <= 9) {
                                menuSize = 9;
                            } else if (size > 9 && size <= 18) {
                                menuSize = 18;
                            } else if (size > 18 && size <= 27) {
                                menuSize = 27;
                            } else if (size > 27 && size <= 36) {
                                menuSize = 36;
                            } else if (size > 36 && size <= 45) {
                                menuSize = 45;
                            } else if (size > 45 && size <= 54) {
                                menuSize = 54;
                            } else if (size > 54) {
                                menuSize = 54;
                            } else {
                                menuSize = 9;
                            }

                            Menu menu = new Menu(ChatColor.BOLD + "Kit Selector", 36);
                            MenuItem item = new MenuItem(Material.IRON_SWORD, ChatColor.RED + "Join The PvP Server", (byte) 1, menu.getInventory());

                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(" ");
                            lore.add(ChatColor.BLUE + "Left Click");
                            lore.add(" ");
                            item.setLore(lore);

                            item.setSlot(5, 1);
                            menu.addItem(item);
                            menu.open(player);
                        }
                    }
                }
            }
        }
    }
}
