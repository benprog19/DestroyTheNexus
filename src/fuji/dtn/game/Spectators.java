package fuji.dtn.game;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/26/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Spectators implements Listener {

    static ArrayList<UUID> spectators = new ArrayList<>();

    public static void addPlayer(Player player) {
        if (!isSpectator(player)) {
            spectators.add(player.getUniqueId());
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 255));
        }

    }

    public static void removePlayer(Player player) {
        if (isSpectator(player)) {
            spectators.remove(player.getUniqueId());
            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }
    }

    public static boolean isSpectator(Player player) {
        if (spectators.contains(player.getUniqueId())) {
            return true;
        }
        return false;
    }



    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        final Player spectator = e.getPlayer();
        if (isSpectator(spectator)) {
            List<Entity> entities = spectator.getNearbyEntities(3, 3, 3);
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i).getType().equals(EntityType.PLAYER)) {
                    spectator.setVelocity(spectator.getLocation().getDirection().multiply(-3));
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        final Player spectator = e.getPlayer();
        if (isSpectator(spectator)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        final Player spectator = e.getPlayer();
        if (isSpectator(spectator)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent e) {
        final Entity entity = e.getDamager();
        if (entity instanceof Player) {
            Player spectator = (Player) e.getDamager();
            if (isSpectator(spectator)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        final Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player spectator = (Player) e.getEntity();
            if (isSpectator(spectator)) {
                e.setCancelled(true);
            }
        }
    }

}
