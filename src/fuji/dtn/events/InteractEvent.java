package fuji.dtn.events;

import fuji.dtn.arena.Arena;
import fuji.dtn.game.GameState;
import fuji.dtn.kits.Kit;
import fuji.dtn.kits.Kits;
import fuji.dtn.menus.Menu;
import fuji.dtn.menus.MenuItem;
import fuji.dtn.voting.Votes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
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
                if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (player.getInventory().getItemInMainHand().getType().equals(Material.FEATHER)) {
                        int menuSize = 0;
                        double size = Kits.getAllRegisteredKits().size();

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
                    } else if (player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
                        if (GameState.getGameState().equals(GameState.WAITING)) {
                            int menuSize = 0;
                            double size = Votes.getAvailableVotes().size();

                            menuSize = Math.min((int) Math.round((size / 9) + 0.5) * 9, 54);

                            Menu votingMenu = new Menu(ChatColor.BOLD + "Map Voting", menuSize);
                            if (Votes.getAvailableVotes().size() > 0) {
                                for (int i = 0; i < Votes.getAvailableVotes().size(); i++) {
                                    MenuItem item = new MenuItem(Material.EMPTY_MAP, ChatColor.GOLD + Votes.getAvailableVotes().get(i).getArena().getName(), 1, (byte) 0, votingMenu.getInventory());
                                    item.setSlot(i);

                                    List<String> lore = new ArrayList<>();
                                    lore.add(" ");
                                    lore.add(" " + ChatColor.GOLD + "Votes: " + ChatColor.RED + Votes.getAvailableVotes().get(i).getVotes());
                                    item.setLore(lore);

                                    storedSlotToArena.put(item.getSlot(), Votes.getAvailableVotes().get(i).getArena());
                                    votingMenu.addItem(item);
                                    //System.out.print(menu.getInventory().getItem(i).getItemMeta().getDisplayName());
                                }
                            } else {
                                MenuItem item = new MenuItem(Material.STAINED_GLASS_PANE, ChatColor.RED + "Voting Is Not Available", 1, (byte) 14, votingMenu.getInventory());
                                item.setSlot(4);
                                votingMenu.addItem(item);
                            }
                            votingMenu.open(player);
                        } else {
                            player.sendMessage(ChatColor.RED + "You cannot vote at this time.");
                        }
                    }
                }
            }
        }
    }
}
