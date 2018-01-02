package fuji.dtn.arena;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/24/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Selection implements Listener {

    Location blueCorner1;
    Location blueCorner2;
    Location redCorner1;
    Location redCorner2;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType() == Material.WOOL) {
                if (SelectionMode.hasPlayer(e.getPlayer())) {
                    if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK) {
                        e.setCancelled(true);
                        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                            Location location = new Location(e.getClickedBlock().getLocation().getWorld(), e.getClickedBlock().getLocation().getBlockX(), e.getClickedBlock().getLocation().getBlockY(), e.getClickedBlock().getLocation().getBlockZ());
                            Arena arena = Arenas.getArenaByName(SelectionMode.getArena(e.getPlayer()).getName());

                            redCorner1 = location;
                            e.getPlayer().sendMessage(ChatColor.RED + "Corner 1 saved.");
                            if (hasSetNexusCorners(redCorner1, redCorner2)) {
                                setRedCorners(e.getPlayer(), arena, redCorner1, redCorner2);
                            }

                        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            Location location = new Location(e.getClickedBlock().getLocation().getWorld(), e.getClickedBlock().getLocation().getBlockX(), e.getClickedBlock().getLocation().getBlockY(), e.getClickedBlock().getLocation().getBlockZ());
                            Arena arena = Arenas.getArenaByName(SelectionMode.getArena(e.getPlayer()).getName());

                            redCorner2 = location;
                            e.getPlayer().sendMessage(ChatColor.RED + "Corner 2 saved.");
                            if (hasSetNexusCorners(redCorner1, redCorner2)) {
                                setRedCorners(e.getPlayer(), arena, redCorner1, redCorner2);
                            }
                        }
                    } else if (e.getItem().getType().equals(Material.BLAZE_ROD)) {
                        e.setCancelled(true);
                        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                            Location location = new Location(e.getClickedBlock().getLocation().getWorld(), e.getClickedBlock().getLocation().getBlockX(), e.getClickedBlock().getLocation().getBlockY(), e.getClickedBlock().getLocation().getBlockZ());
                            Arena arena = Arenas.getArenaByName(SelectionMode.getArena(e.getPlayer()).getName());

                            blueCorner1 = location;
                            e.getPlayer().sendMessage(ChatColor.BLUE + "Corner 1 saved.");
                            if (hasSetNexusCorners(blueCorner1, blueCorner2)) {
                                setBlueCorners(e.getPlayer(), arena, blueCorner1, blueCorner2);
                            }

                        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            Location location = new Location(e.getClickedBlock().getLocation().getWorld(), e.getClickedBlock().getLocation().getBlockX(), e.getClickedBlock().getLocation().getBlockY(), e.getClickedBlock().getLocation().getBlockZ());
                            Arena arena = Arenas.getArenaByName(SelectionMode.getArena(e.getPlayer()).getName());

                            blueCorner2 = location;
                            e.getPlayer().sendMessage(ChatColor.BLUE + "Corner 2 set.");
                            if (hasSetNexusCorners(blueCorner1, blueCorner2)) {
                                setBlueCorners(e.getPlayer(), arena, blueCorner1, blueCorner2);
                            }

                        }
                    }
                }
            } else {
                if (SelectionMode.hasPlayer(e.getPlayer())) {
                    if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.PRISMARINE_SHARD) {
                        e.setCancelled(true);
                        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                            Location location = new Location(e.getClickedBlock().getLocation().getWorld(), e.getClickedBlock().getLocation().getBlockX(), e.getClickedBlock().getLocation().getBlockY(), e.getClickedBlock().getLocation().getBlockZ());
                            Arena arena = Arenas.getArenaByName(SelectionMode.getArena(e.getPlayer()).getName());
                            arena.setArenaCorner1(location);
                            e.getPlayer().sendMessage(ChatColor.GOLD + "Corner 1 set.");
                            if (hasSetArenaCorners(arena)) {
                                setArenaCorners(e.getPlayer(), arena);
                            }
                        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            Location location = new Location(e.getClickedBlock().getLocation().getWorld(), e.getClickedBlock().getLocation().getBlockX(), e.getClickedBlock().getLocation().getBlockY(), e.getClickedBlock().getLocation().getBlockZ());
                            Arena arena = Arenas.getArenaByName(SelectionMode.getArena(e.getPlayer()).getName());
                            arena.setArenaCorner2(location);
                            e.getPlayer().sendMessage(ChatColor.GOLD + "Corner 2 set.");
                            if (hasSetArenaCorners(arena)) {
                                setArenaCorners(e.getPlayer(), arena);
                            }
                        }
                    }
                }
            }
        }

    }

    private boolean hasSetArenaCorners(Arena arena) {
        if (arena.getArenaCorner1() != null && arena.getArenaCorner2() != null) {
            return true;
        }
        return false;
    }

    private void setArenaCorners(Player player, Arena arena) {
        if (hasSetArenaCorners(arena)) {
            SelectionMode.removePlayer(player);
            player.sendMessage(ChatColor.GOLD + "Arena Corners have been set.");
            player.getInventory().clear();

        }
    }

    private boolean hasSetNexusCorners(Location corner1, Location corner2) {
        if (corner1 != null && corner2 != null) {
            return true;
        }

        return false;
    }


    private void setBlueCorners(Player player, Arena arena, Location corner1, Location corner2) {
        if (hasSetNexusCorners(corner1, corner2)) {
            double minX;
            double maxX;
            double minY;
            double maxY;
            double minZ;
            double maxZ;

            minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
            maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
            minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
            maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
            minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
            maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

            Location lesserCorner = new Location(corner1.getWorld(), minX, minY, minZ);
            Location greaterCorner = new Location(corner1.getWorld(), maxX, maxY, maxZ);

            arena.setBlueNexusCorner1(lesserCorner);
            arena.setBlueNexusCorner2(greaterCorner);

            SelectionMode.removePlayer(player);
            player.sendMessage(ChatColor.GOLD + "Nexus for " + ChatColor.BLUE + "Blue Team " + ChatColor.GOLD + "has been set.");
            player.getInventory().clear();
        }
    }

    private void setRedCorners(Player player, Arena arena, Location corner1, Location corner2) {
        if (hasSetNexusCorners(corner1, corner2)) {
            double minX;
            double maxX;
            double minY;
            double maxY;
            double minZ;
            double maxZ;

            minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
            maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
            minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
            maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
            minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
            maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

            Location lesserCorner = new Location(corner1.getWorld(), minX, minY, minZ);
            Location greaterCorner = new Location(corner1.getWorld(), maxX, maxY, maxZ);

            arena.setRedNexusCorner1(lesserCorner);
            arena.setRedNexusCorner2(greaterCorner);

            SelectionMode.removePlayer(player);
            player.sendMessage(ChatColor.GOLD + "Nexus for " + ChatColor.RED + "Red Team " + ChatColor.GOLD + "has been set.");
            player.getInventory().clear();
        }
    }

}
