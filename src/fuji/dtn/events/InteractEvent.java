package fuji.dtn.events;

import fuji.dtn.arena.Arena;
import fuji.dtn.game.GameState;
import fuji.dtn.kits.Kit;
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

import java.util.concurrent.ConcurrentHashMap;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 1/1/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class InteractEvent implements Listener {

    public static ConcurrentHashMap<Integer, Kit> storedSlotToKit = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Integer, Arena> storedSlotToArena = new ConcurrentHashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (DeathEvent.isDead(player)) {
            e.setCancelled(true);
        } else {
            if (GameState.getGameState().equals(GameState.WAITING) || GameState.getGameState().equals(GameState.STARTING)) {
                if (player.getInventory().getItemInMainHand().getType().equals(Material.FEATHER)) {
                    if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        int menuSize = 0;
                        double size = Kits.getAllRegisteredKits().size();
//                        if (size <= 9) {
//                            menuSize = 9;
//                        } else if (size > 9 && size <= 18) {
//                            menuSize = 18;
//                        } else if (size > 18 && size <= 27) {
//                            menuSize = 27;
//                        } else if (size > 27 && size <= 36) {
//                            menuSize = 36;
//                        } else if (size > 36 && size <= 45) {
//                            menuSize = 45;
//                        } else if (size > 45 && size <= 54) {
//                            menuSize = 54;
//                        } else if (size > 54) {
//                            menuSize = 54;
//                        } else {
//                            menuSize = 9;
//                        }


                        menuSize = Math.min((int) Math.round((size / 9) + 0.5) * 9, 54);

                        Menu kitMenu = new Menu(ChatColor.BOLD + "Kit Selector", menuSize);
                        for (int i = 0; i < Kits.getAllRegisteredKits().size(); i++) {
                            MenuItem item = new MenuItem(Material.BANNER, ChatColor.RED + Kits.getAllRegisteredKits().get(i).getName(), 1, (byte) 0, kitMenu.getInventory());
                            item.setSlot(i);
                            //System.out.print("Slot: " + item.getSlot());
                            storedSlotToKit.put(item.getSlot(), Kits.getAllRegisteredKits().get(i));
                            kitMenu.addItem(item);
                            //System.out.print(menu.getInventory().getItem(i).getItemMeta().getDisplayName());
                        }
                        kitMenu.open(player);
                    }
                }

            }
        }
    }
}
