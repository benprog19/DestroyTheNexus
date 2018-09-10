package fuji.dtn.events;

import com.connorlinfoot.titleapi.TitleAPI;
import fuji.dtn.game.Game;
import fuji.dtn.game.Players;
import fuji.dtn.game.Spectators;
import fuji.dtn.kits.Kit;
import fuji.dtn.kits.Kits;
import fuji.dtn.main.Main;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import fuji.dtn.util.ParticleUtil;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutCamera;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 10/3/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class DeathEvent implements Listener {

    public static ArrayList<UUID> deadPlayers = new ArrayList<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (Players.isPlayer(player)) {
            Team red = Teams.getTeamByName("red");
            Team blue = Teams.getTeamByName("blue");

            if (!deadPlayers.contains(player.getUniqueId())) {
                deadPlayers.add(player.getUniqueId());
            }

            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);

            if (red != null && blue != null) {
                e.getDrops().clear();
                player.getInventory().clear();
                if (player.getKiller() != null && player.getKiller() instanceof Player) {
                    Player killer = player.getKiller();
                    Team killersTeam = Teams.getTeamFromPlayer(killer);
                    if (killersTeam != null) {
                        TitleAPI.sendTitle(player, 0, 100, 0, ChatColor.RED + "YOU DIED", "You were killed by " + killersTeam.getColor() + killer.getName() + ChatColor.RED + " ♥ " + Math.round(killer.getHealth()) + ChatColor.WHITE + ".");
                    }
                    e.setDeathMessage(Teams.getColorFromPlayerTeam(player) + player.getName() + " (" + Kits.getKitNameByPlayer(player) + ")" + ChatColor.GRAY + " was defeated by " + Teams.getColorFromPlayerTeam(killer) + killer.getName() + " (" + Kits.getKitByPlayer(killer).getName() + ").");
                    predeath(player, killer);
                } else {
                    TitleAPI.sendTitle(player, 0, 100, 0, ChatColor.RED + "YOU DIED", "");
                    e.setDeathMessage(Teams.getColorFromPlayerTeam(player) + player.getName() + " (" + Kits.getKitNameByPlayer(player) + ")" + ChatColor.GRAY + " has died.");
                    predeath(player, null);
                }


            }
        } else if (Spectators.isSpectator(player)) {
            player.setHealth(20);
        }
    }

    @SuppressWarnings("deprecation")
	private void predeath(Player player, Player killer) {
        player.setHealth(20);
        for (int pls = 0; pls < Players.getPlayers().size(); pls++) {
            Player pls1 = Bukkit.getPlayer(Players.getPlayers().get(pls));
            pls1.hidePlayer(player);
        }
        player.setFoodLevel(20);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 2));
        player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
        player.setAllowFlight(true);
        ParticleUtil.createHelix(player.getLocation(), EnumParticle.FLAME,  1, 3);

        if (!Game.invincible(Teams.getTeamFromPlayer(player))) {
            player.sendMessage(ChatColor.RED + "You have died and your nexus has been destroyed. You may no longer respawn.");
            Bukkit.broadcastMessage(Teams.getTeamFromPlayer(player).getColor() + player.getName() + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " >> " + ChatColor.RED + "ELIMINATED");
            player.getWorld().strikeLightningEffect(player.getLocation());
            if (teamIsDead(Teams.getTeamFromPlayer(player))) {
                if (killer != null) {
                    Game.endGame(Teams.getTeamFromPlayer(killer));
                } else {
                    if (Teams.getTeamFromPlayer(player).equals(Teams.getTeamByName("red"))) {
                        Game.endGame(Teams.getTeamByName("blue"));
                    } else if (Teams.getTeamFromPlayer(player).equals(Teams.getTeamByName("blue"))) {
                        Game.endGame(Teams.getTeamByName("red"));
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.GOLD + "You will be revived in " + ChatColor.RED + "10 seconds.");

            if (killer != null) {
                player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 10, player.getLocation().getZ()));
                player.setFlying(true);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        CraftPlayer ckiller = (CraftPlayer) killer;
                        CraftPlayer cplayer = (CraftPlayer) player;
                        PacketPlayOutCamera packetPlayOutCamera = new PacketPlayOutCamera(ckiller.getHandle());
                        cplayer.getHandle().playerConnection.sendPacket(packetPlayOutCamera);
                    }
                }.runTaskLater(JavaPlugin.getPlugin(Main.class), 80L);

            }

            afterdeath(player, killer);
        }

    }

    private void afterdeath(final Player player, Player killer) {
        new BukkitRunnable() {

            @SuppressWarnings("deprecation")
			@Override
            public void run() {
                if (deadPlayers.contains(player.getUniqueId())) {
                    deadPlayers.remove(player.getUniqueId());
                }

                CraftPlayer cplayer = (CraftPlayer) player;
                PacketPlayOutCamera packetPlayOutCamera = new PacketPlayOutCamera(cplayer.getHandle());
                cplayer.getHandle().playerConnection.sendPacket(packetPlayOutCamera);

                Players.teleportPlayerToTeams(player, false);
                player.setHealth(20);
                player.setFireTicks(0);
                for (int pls = 0; pls < Players.getPlayers().size(); pls++) {
                    Player pls1 = Bukkit.getPlayer(Players.getPlayers().get(pls));
                    pls1.showPlayer(player);

                    Team team = Teams.getTeamFromPlayer(player);
                    if (team != null) {
                        if (team.hasPlayer(pls1)) {
                            pls1.sendMessage(team.getColor() + player.getName() + ChatColor.GRAY + " has been revived!");
                        }
                    }
                }
                player.setFoodLevel(20);
                player.setAllowFlight(false);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 255));
                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
                TitleAPI.sendTitle(player, 0, 100, 0, "", ChatColor.GREEN + "REVIVED");
                player.sendMessage(ChatColor.GREEN + "You have been revived!");
                Kit kit = Kits.getKitByPlayer(player);
                if (kit != null) {
                    Kits.setInventory(player, kit);
                }

            }
        }.runTaskLater(JavaPlugin.getPlugin(Main.class), 200L);
    }

    public static boolean isDead(Player player) {
        return deadPlayers.contains(player.getUniqueId());
    }

    public static boolean teamIsDead(Team team) {
        int playersTeamCount = team.getPlayers().size();
        int deadTeamPlayers = 0;

        for (int i = 0; i < playersTeamCount; i++) {
            Player user = Bukkit.getPlayer(team.getPlayers().get(i));
            if (isDead(user)) {
                deadTeamPlayers++;
            }
        }

        if (deadTeamPlayers == playersTeamCount) {
            return true;
        }
        return false;
    }

}
