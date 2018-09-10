package fuji.dtn.events;

import fuji.dtn.game.GameState;
import fuji.dtn.game.Players;
import fuji.dtn.main.Main;
import fuji.dtn.teams.Teams;
import fuji.dtn.util.ParticleUtil;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 12/7/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class PvPEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPvP(EntityDamageByEntityEvent e) {
        final Entity p = e.getEntity();
        final Entity d = e.getDamager();
        if (p instanceof Player && d instanceof Player) {
            final Player player = (Player) e.getEntity();
            final Player damager = (Player) e.getDamager();
            if (GameState.getGameState().equals(GameState.INGAME)) {
                if (Players.isPlayer(player) && Players.isPlayer(damager)) {
                    if (!DeathEvent.isDead(damager)) {
                        if (Teams.getTeamFromPlayer(player) != Teams.getTeamFromPlayer(damager)) {
                            for (int i = 0; i < 20; i++) {
                                player.spawnParticle(
                                        Particle.BLOCK_CRACK,
                                        player.getEyeLocation(), 1, 0, 0, 0, new MaterialData(Material.REDSTONE_BLOCK));
                            }
                            player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
                            player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_BLAZE_HURT, 1, 1);

                        } else if (Teams.getTeamFromPlayer(player) == Teams.getTeamFromPlayer(damager)) {
                            e.setCancelled(true);
                            damager.sendMessage(ChatColor.RED + "You cannot damage a teammate.");
                        } else {
                            e.setCancelled(true);
                            damager.sendMessage(ChatColor.RED + "You are not allowed to damage other players at this time.");
                        }
                    } else {
                        e.setCancelled(true);
                        damager.sendMessage(ChatColor.RED + "You cannot damage a player when spectating.");
                    }
                } else {
                    e.setCancelled(true);
                    damager.sendMessage(ChatColor.RED + "You cannot damage a player when you are dead.");
                }
            } else {
                e.setCancelled(true);
            }
        } else if (d instanceof Arrow && p instanceof Player) {
            final Player player = (Player) e.getEntity();
            final Arrow arrow = (Arrow) e.getDamager();
            final ProjectileSource source = arrow.getShooter();
            if (source instanceof Player) {
                Player damager = (Player) source;
                if (GameState.getGameState().equals(GameState.INGAME)) {
                    if (Players.isPlayer(player)) {
                        if (Teams.getTeamFromPlayer(player) != Teams.getTeamFromPlayer(damager)) {
                            ParticleUtil.particle(arrow.getLocation(), EnumParticle.EXPLOSION_LARGE);
                        } else if (Teams.getTeamFromPlayer(player) == Teams.getTeamFromPlayer(damager)) {
                            e.setCancelled(true);
                            damager.sendMessage(ChatColor.RED + "You cannot damage a teammate.");
                            arrow.remove();
                        } else {
                            e.setCancelled(true);
                            damager.sendMessage(ChatColor.RED + "You are not allowed to damage other players at this time.");
                            arrow.remove();
                        }
                    } else {
                        e.setCancelled(true);
                        damager.sendMessage(ChatColor.RED + "You cannot damage a player when you are not in game.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBow(EntityShootBowEvent e) {
        final Entity pro = e.getProjectile();

        new BukkitRunnable() {

            int i = 0;
            @Override
            public void run() {
                i++;
                if (i <= 15) {
                    if (!pro.isDead() || !pro.isOnGround()) {
                        ParticleUtil.particle(pro.getLocation(), EnumParticle.FLAME);
                    }
                } else {
                    ParticleUtil.particle(pro.getLocation(), EnumParticle.EXPLOSION_LARGE);
                    pro.remove();
                    cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0, 1L);
    }
}
