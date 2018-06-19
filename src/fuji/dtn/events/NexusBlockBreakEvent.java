package fuji.dtn.events;

import com.connorlinfoot.titleapi.TitleAPI;
import fuji.dtn.arena.Arenas;
import fuji.dtn.exceptions.BlockBreakException;
import fuji.dtn.game.Game;
import fuji.dtn.game.GameState;
import fuji.dtn.game.Players;
import fuji.dtn.main.Main;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import fuji.dtn.util.ParticleUtil;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.List;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 9/11/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class NexusBlockBreakEvent implements Listener {

    public static int blocksRed = 0;
    public static int blocksBlue = 0;
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onBreak(BlockBreakEvent e) {
        final Player player = e.getPlayer();
        e.setDropItems(false);
        if (GameState.getGameState() == GameState.INGAME) {
            if (Players.isPlayer(player)) {
                if (!BlockPlaceEvent.wasPlacedByPlayer(e.getBlock())) {
                    if (e.getBlock().getType() == Material.WOOL) {
                        ConfigurationSection section = Main.arenaStorage.get().getConfigurationSection("Arenas." + Rotation.getCurrentArena().getName() + ".");
                        Location redNexusCorner1 = createLocation("redNexusCorner1", section);
                        Location redNexusCorner2 = createLocation("redNexusCorner2", section);

                        Location blueNexusCorner1 = createLocation("blueNexusCorner1", section);
                        Location blueNexusCorner2 = createLocation("blueNexusCorner2", section);

                        //Location blockBroken = e.getBlock().getLocation();

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
                        if (e.getBlock().getData() == 14 || e.getBlock().getData() == 11) {
//                        if (blue.hasPlayer(player)) {
//                            if (e.getBlock().getData() == 14) {
//                                if (redNexusCorner1.getBlockX() <= e.getBlock().getX() && redNexusCorner1.getBlockY() <= e.getBlock().getY()
//                                        && redNexusCorner1.getBlockZ() <= e.getBlock().getZ() && redNexusCorner2.getBlockX() >= e.getBlock().getX()
//                                        && redNexusCorner2.getBlockY() >= e.getBlock().getY() && redNexusCorner2.getBlockZ() >= e.getBlock().getZ()) {
//
//                                    blocksRed++;
//                                    int dmg = calculateNexusDamage(red, redNexusCorner1, redNexusCorner2);
//
//                                    bcDmg(dmg, red);
//                                    player.getWorld().playSound(e.getBlock().getLocation(), Sound.BLOCK_ANVIL_PLACE, 10, 0);
//                                    e.setDropItems(false);
//                                    if (dmg == 100) {
//                                        Game.lostInvincibility(red);
//                                        for (Player member : Bukkit.getOnlinePlayers()) {
//                                            TitleAPI.sendTitle(member, 5, 200, 20, "WARNING!", ChatColor.RED + "" + ChatColor.BOLD + "RED NEXUS DESTROYED");
//                                            member.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 500, 1));
//                                        }
//                                        red.bc(ChatColor.RED + "" + ChatColor.BOLD + "Your nexus has been breached, you will no longer respawn!");
//                                        player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 2);
//                                        player.getWorld().strikeLightningEffect(e.getBlock().getLocation());
//                                        bcDmg(-1, red);
//                                    }
//                                } else {
//                                    player.sendMessage(ChatColor.RED + "You cannot destroy this block.");
//                                    e.setCancelled(true);
//                                }
//                            } else {
//                                player.sendMessage(ChatColor.RED + "You cannot destroy this block.");
//                                e.setCancelled(true);
//                            }
//                        } else if (red.hasPlayer(player)) {
//                            if (e.getBlock().getData() == 11) {
//                                if (blueNexusCorner1.getBlockX() <= e.getBlock().getX() && blueNexusCorner1.getBlockY() <= e.getBlock().getY()
//                                        && blueNexusCorner1.getBlockZ() <= e.getBlock().getZ() && blueNexusCorner2.getBlockX() >= e.getBlock().getX()
//                                        && blueNexusCorner2.getBlockY() >= e.getBlock().getY() && blueNexusCorner2.getBlockZ() >= e.getBlock().getZ()) {
//
//                                    blocksBlue++;
//                                    int dmg = calculateNexusDamage(blue, blueNexusCorner1, blueNexusCorner2);
//                                    player.getWorld().playSound(e.getBlock().getLocation(), Sound.BLOCK_ANVIL_PLACE, 10, 0);
//                                    bcDmg(dmg, blue);
//                                    e.setDropItems(false);
//                                    if (dmg == 100) {
//                                        Game.lostInvincibility(blue);
//                                        for (Player member : Bukkit.getOnlinePlayers()) {
//                                            TitleAPI.sendTitle(member, 5, 200, 20, "WARNING!", ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE NEXUS DESTROYED");
//                                            member.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 500, 1));
//                                        }
//                                        blue.bc(ChatColor.RED + "" + ChatColor.BOLD + "Your nexus has been breached, you will no longer respawn!");
//                                        player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 2);
//                                        player.getWorld().strikeLightningEffect(e.getBlock().getLocation());
//                                        bcDmg(-1, blue);
//                                    }
//                                } else {
//                                    player.sendMessage(ChatColor.RED + "You cannot destroy this block.");
//                                    e.setCancelled(true);
//                                }
//                            } else {
//                                player.sendMessage(ChatColor.RED + "You cannot destroy this block.");
//                                e.setCancelled(true);
//                            }
//                        }
                            try {
                                nexusBreak(e.getBlock(), redNexusCorner1, redNexusCorner2, blueNexusCorner1, blueNexusCorner2, player);
                            } catch (BlockBreakException ex) {
                                e.setCancelled(true);
                                player.sendMessage(ChatColor.RED + "You cannot break that block.");
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You can only destroy blocks placed by a player.");
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(false);
                    BlockPlaceEvent.removePlayerPlacedBlock(e.getBlock());
                }
            } else {
                e.setCancelled(true);
            }
        } else {
            if (!e.getPlayer().isOp()) {
                e.setCancelled(true);
            }
        }
    }

    private void nexusBreak(Block block, Location redNexusCorner1, Location redNexusCorner2, Location blueNexusCorner1, Location blueNexusCorner2, Player player) throws BlockBreakException {
        Team playerTeam = Teams.getTeamFromPlayer(player);
        Team opposingTeam = null;
        byte blockID;
        boolean correctBlockID = false;
        boolean withinRegion = false;
        boolean destroyed = false;
        Location nexusCorner1 = null;
        Location nexusCorner2 = null;
        if (playerTeam.getName().equalsIgnoreCase("red")) {
            opposingTeam = Teams.getTeamByName("blue");
            blockID = 11;
            if (block.getData() == blockID) {
                correctBlockID = true;
                if (blueNexusCorner1.getBlockX() <= block.getX() && blueNexusCorner1.getBlockY() <= block.getY()
                        && blueNexusCorner1.getBlockZ() <= block.getZ() && blueNexusCorner2.getBlockX() >= block.getX()
                        && blueNexusCorner2.getBlockY() >= block.getY() && blueNexusCorner2.getBlockZ() >= block.getZ()) {
                    nexusCorner1 = blueNexusCorner1;
                    nexusCorner2 = blueNexusCorner2;
                    withinRegion = true;
                    blocksBlue++;
                }
            } else if (block.getData() == 14) {
                throw new BlockBreakException();
            }
        } else if (playerTeam.getName().equalsIgnoreCase("blue")) {
            opposingTeam = Teams.getTeamByName("red");
            blockID = 14;
            if (block.getData() == blockID) {
                correctBlockID = true;
                if (redNexusCorner1.getBlockX() <= block.getX() && redNexusCorner1.getBlockY() <= block.getY()
                        && redNexusCorner1.getBlockZ() <= block.getZ() && redNexusCorner2.getBlockX() >= block.getX()
                        && redNexusCorner2.getBlockY() >= block.getY() && redNexusCorner2.getBlockZ() >= block.getZ()) {
                    nexusCorner1 = redNexusCorner1;
                    nexusCorner2 = redNexusCorner2;
                    withinRegion = true;
                    blocksRed++;
                }
            } else if (block.getData() == 11) {
                throw new BlockBreakException();
            }
        }

        int dmg = calculateNexusDamage(opposingTeam, nexusCorner1, nexusCorner2);
        if (dmg == 100) {
            destroyed = true;
        }

        if (correctBlockID && withinRegion) {
            bcDmg(dmg, opposingTeam);
            player.getWorld().playSound(block.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 10, 0);
            block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            if (destroyed) {
                Game.lostInvincibility(opposingTeam);
                for (Player member : Bukkit.getOnlinePlayers()) {
                    TitleAPI.sendTitle(member, 5, 200, 20, "WARNING!", opposingTeam.getColor() + "" + ChatColor.BOLD + opposingTeam.getName().toUpperCase() + " NEXUS DESTROYED");
                }
                if (opposingTeam.equals(Teams.getTeamByName("red"))) {
                    ParticleUtil.createHelix(Arenas.getArenaByName(Rotation.getCurrentArena().getName()).getRedLocation(), EnumParticle.DRAGON_BREATH, 3, 10);
                } else if (opposingTeam.equals(Teams.getTeamByName("blue"))) {
                    ParticleUtil.createHelix(Arenas.getArenaByName(Rotation.getCurrentArena().getName()).getBlueLocation(), EnumParticle.DRAGON_BREATH, 3, 10);
                }
                for (int i = 0; i < opposingTeam.getPlayers().size(); i++) {
                    OfflinePlayer offlineUser = Bukkit.getOfflinePlayer(opposingTeam.getPlayers().get(i));
                    if (offlineUser.isOnline()) {
                        ((Player) offlineUser).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 500, 1));
                        ((Player) offlineUser).setHealth(20);
                        ((Player) offlineUser).getWorld().playEffect(player.getEyeLocation(), Effect.GHAST_SHOOT, 1);
                        ((Player) offlineUser).getWorld().playEffect(((Player) offlineUser).getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                    }
                }

                opposingTeam.bc(ChatColor.RED + "" + ChatColor.BOLD + "Your nexus has been breached, you will no longer respawn!");
                player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 2);
                player.getWorld().strikeLightningEffect(block.getLocation());
                bcDmg(-1, opposingTeam);
            }
        }
    }

    private void bcDmg(int dmg, Team team) {
        int perc = 100 - dmg;
        ChatColor color = ChatColor.WHITE;
        if (perc >= 75) {
            // Green
            color = ChatColor.GREEN;
        } else if (perc >= 50) {
            // Yellow
            color = ChatColor.YELLOW;
        } else if (perc >= 25) {
            // Gold
            color = ChatColor.GOLD;
        } else if (perc >= -1) {
            // Red
            color = ChatColor.RED;
        }

        if (dmg == -1) {
            Bukkit.broadcastMessage(team.getColor() + "" + ChatColor.BOLD + team.getName().toUpperCase() + " NEXUS " + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "DESTROYED");
            for (Player pls : Bukkit.getOnlinePlayers()) {
                TitleAPI.sendTitle(pls, 0, 20, 0, "", team.getColor() + "" + team.getName().toString().charAt(0) + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " >> " + ChatColor.RED + "DESTROYED");
            }
        } else {
            Bukkit.broadcastMessage(team.getColor() + "" + ChatColor.BOLD + team.getName().toUpperCase() + " NEXUS " + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + color + perc + "%");
            for (Player pls : Bukkit.getOnlinePlayers()) {
                TitleAPI.sendTitle(pls, 0, 20, 0, "", team.getColor() + "" + team.getName().toString().charAt(0) + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " >> " + color + perc + "%");
            }
        }

    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.PRIMED_TNT) {
            List<Block> blocks = event.blockList();
            Iterator<Block> it = blocks.iterator();
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
    	//ArrayList<Block> blocks = new ArrayList<>();
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
