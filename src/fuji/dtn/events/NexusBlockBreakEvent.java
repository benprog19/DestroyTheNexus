package fuji.dtn.events;

import fuji.dtn.game.GameState;
import fuji.dtn.game.Players;
import fuji.dtn.main.Main;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 9/11/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class NexusBlockBreakEvent implements Listener {

    int blocksRed = 0;
    int blocksBlue = 0;
    
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        final Player player = e.getPlayer();
        if (GameState.getGameState() == GameState.INGAME) {
            if (Players.isPlayer(player)) {
                if (e.getBlock().getType() == Material.WOOL) {
                    Team red = Teams.getTeamByName("red");
                    Team blue = Teams.getTeamByName("blue");
                    ConfigurationSection section = Main.arenaStorage.get().getConfigurationSection("Arenas." + Rotation.getCurrentArena().getName() + ".");
                    Location redNexusCorner1 = createLocation("redNexusCorner1", section);
                    Location redNexusCorner2 = createLocation("redNexusCorner2", section);

                    Location blueNexusCorner1 = createLocation("blueNexusCorner1", section);
                    Location blueNexusCorner2 = createLocation("blueNexusCorner2", section);

                    Location blockBroken = e.getBlock().getLocation();

                    /*
                    Bukkit.broadcastMessage(" ");
                    Bukkit.broadcastMessage(" ");
                    Bukkit.broadcastMessage("blueNexusCorner1.getBlockX() <= e.getBlock().getX() (" + (blueNexusCorner1.getBlockX() <= e.getBlock().getX()) + ")");
                    Bukkit.broadcastMessage("blueNexusCorner1.getBlockY() <= e.getBlock().getY() (" + (blueNexusCorner1.getBlockY() <= e.getBlock().getY()) + ")");
                    Bukkit.broadcastMessage("blueNexusCorner1.getBlockZ() <= e.getBlock().getZ() (" + (blueNexusCorner1.getBlockZ() <= e.getBlock().getZ()) + ")");
                    Bukkit.broadcastMessage("blueNexusCorner2.getBlockX() <= e.getBlock().getX() (" + (blueNexusCorner2.getBlockX() >= e.getBlock().getX()) + ")");
                    Bukkit.broadcastMessage("blueNexusCorner2.getBlockX() <= e.getBlock().getY() (" + (blueNexusCorner2.getBlockY() >= e.getBlock().getY()) + ")");
                    Bukkit.broadcastMessage("blueNexusCorner2.getBlockX() <= e.getBlock().getZ() (" + (blueNexusCorner2.getBlockZ() >= e.getBlock().getZ()) + ")");
                    */

                    if (blue.hasPlayer(player)) {
                        if (e.getBlock().getData() == 14) {

                            if (redNexusCorner1.getBlockX() <= e.getBlock().getX() && redNexusCorner1.getBlockY() <= e.getBlock().getY()
                                    && redNexusCorner1.getBlockZ() <= e.getBlock().getZ() && redNexusCorner2.getBlockX() >= e.getBlock().getX()
                                    && redNexusCorner2.getBlockY() >= e.getBlock().getY() && redNexusCorner2.getBlockZ() >= e.getBlock().getZ()) {
                                blocksRed++;
                                int dmg = calculateNexusDamage(red, redNexusCorner1, redNexusCorner2);
                                Bukkit.broadcastMessage(ChatColor.GOLD + "Team " + ChatColor.RED + red.getName() + "'s Nexus At " + ChatColor.GOLD + "" + ChatColor.BOLD + dmg + "%");
                                e.setDropItems(false);
                                if (dmg == 100) {
                                    Bukkit.broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE TEAM WINS!");
                                    blocksBlue = 0;
                                    blocksRed = 0;
                                }
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    } else if (red.hasPlayer(player)) {
                        if (e.getBlock().getData() == 11) {
                            if (blueNexusCorner1.getBlockX() <= e.getBlock().getX() && blueNexusCorner1.getBlockY() <= e.getBlock().getY()
                                    && blueNexusCorner1.getBlockZ() <= e.getBlock().getZ() && blueNexusCorner2.getBlockX() >= e.getBlock().getX()
                                    && blueNexusCorner2.getBlockY() >= e.getBlock().getY() && blueNexusCorner2.getBlockZ() >= e.getBlock().getZ()) {
                                blocksBlue++;
                                int dmg = calculateNexusDamage(blue, blueNexusCorner1, blueNexusCorner2);
                                Bukkit.broadcastMessage(ChatColor.GOLD + "Team " + ChatColor.BLUE + blue.getName() + "'s Nexus At " + ChatColor.GOLD + "" + ChatColor.BOLD + dmg + "%");
                                e.setDropItems(false);
                                if (dmg == 100) {
                                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "RED TEAM WINS!");
                                    blocksBlue = 0;
                                    blocksRed = 0;
                                }
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.PRIMED_TNT) {
            List blocks = event.blockList();
            Iterator it = blocks.iterator();
            while (it.hasNext()) {
                Block block = (Block) it.next();
                if (block.getType().equals(Material.WOOL)) {
                    if (block.getData() == 11 || block.getData() == 14) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private int calculateNexusDamage(Team team, Location corner1, Location corner2) {
        ArrayList<Block> blocks = new ArrayList<>();
//        System.out.print("Corner1: " + corner1.getBlockX() + "   Corner2: " + corner2.getBlockX());
//        System.out.print("Corner1: " + corner1.getBlockY() + "   Corner2: " + corner2.getBlockY());
//        System.out.print("Corner1: " + corner1.getBlockZ() + "   Corner2: " + corner2.getBlockZ());
//
//        System.out.print((corner2.getX() - corner1.getX() + 1) * (corner2.getY() - corner1.getY() + 1) * (corner2.getZ() - corner1.getZ() + 1));
//        System.out.print(blocksBlue);
//        System.out.print(blocksRed);
        if (team == Teams.getTeamByName("blue")) {
            return (int) ((blocksBlue / ((corner2.getX() - corner1.getX() + 1) * (corner2.getY() - corner1.getY() + 1) * (corner2.getZ() - corner1.getZ() + 1)) * 100));
        } else if (team == Teams.getTeamByName("red")) {
            return (int) ((blocksRed / ((corner2.getX() - corner1.getX() + 1) * (corner2.getY() - corner1.getY() + 1) * (corner2.getZ() - corner1.getZ() + 1)) * 100));
        }

    return -1;
    }

    static Location createLocation(String configNode, ConfigurationSection config) {
        Location location = null;

        String world = config.getString(configNode + ".world");
        double x = config.getDouble(configNode + ".x");
        double y = config.getDouble(configNode + ".y");
        double z = config.getDouble(configNode + ".z");
        int yaw = config.getInt(configNode + ".yaw");
        int pitch = config.getInt(configNode + ".pitch");

        // Numbers assumed 0.

        if (world != null) {
            World worldObj = Bukkit.getWorld(world);
            if (worldObj != null) {
                location = new Location(worldObj, x, y, z, yaw, pitch);
            }
        }
        return location;
    }

}
