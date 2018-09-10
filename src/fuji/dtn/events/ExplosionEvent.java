package fuji.dtn.events;

import fuji.dtn.game.GameState;
import fuji.dtn.util.ParticleUtil;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;


/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 7/26/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class ExplosionEvent implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof TNTPrimed) {
            if (GameState.getGameState().equals(GameState.INGAME)) {
                Iterator<Block> iter = e.blockList().iterator();
                while (iter.hasNext()) {
                    Block block = iter.next();
                    if (!BlockPlaceEvent.storedBlocks.contains(block)) {
                        for (int i = 0; i < 5; i++) {
                            ParticleUtil.particle(block.getLocation(), EnumParticle.WATER_WAKE);
                        }
                        iter.remove();
                    } else {
                        ParticleUtil.particle(block.getLocation(), EnumParticle.BARRIER);
                    }
                }
//                for (Player pls : entity.getWorld().getPlayers()) {
//                    if (pls.getLocation().distance(entity.getLocation()) <= 5) {
//                        double dmg = 15.0 - pls.getLocation().distance(entity.getLocation());
//                        pls.setHealth(20 - dmg);
//                    }
//                }
            }
        }
    }
}
