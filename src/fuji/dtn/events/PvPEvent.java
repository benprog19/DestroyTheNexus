package fuji.dtn.events;

import fuji.dtn.game.GameState;
import fuji.dtn.game.Players;
import fuji.dtn.teams.Teams;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.material.MaterialData;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 12/7/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class PvPEvent implements Listener {

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent e) {
        final Entity p = e.getEntity();
        final Entity d = e.getDamager();

        if (p instanceof Player && d instanceof Player) {
            final Player player = (Player) e.getEntity();
            final Player damager = (Player) e.getDamager();
            if (GameState.getGameState().equals(GameState.INGAME)) {
                if (Players.isPlayer(player) && Players.isPlayer(damager)) {
                    if (!DeathEvent.deadPlayers.contains(damager.getUniqueId())) {
                        if (Teams.getTeamFromPlayer(player) != Teams.getTeamFromPlayer(damager)) {
                            for (int i = 0; i < 20; i++) {
                                player.spawnParticle(
                                        Particle.BLOCK_CRACK,
                                        player.getEyeLocation(), 1, 0, 0, 0, new MaterialData(Material.REDSTONE_BLOCK));
                            }
                            player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);

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
        }
    }
}
