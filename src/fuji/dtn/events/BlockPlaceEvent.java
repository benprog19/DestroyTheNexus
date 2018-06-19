package fuji.dtn.events;

import fuji.dtn.game.GameState;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/12/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class BlockPlaceEvent implements Listener {

    static ArrayList<Block> storedBlocks = new ArrayList<>();

    @EventHandler
    public void onPlace(org.bukkit.event.block.BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (GameState.getGameState().equals(GameState.INGAME)) {
            storedBlocks.add(e.getBlockPlaced());
            //System.out.print("New Block Placed: " + e.getBlockPlaced().getType().toString() + ", " + e.getBlockPlaced().getLocation().getBlockX() + ", " + e.getBlockPlaced().getLocation().getBlockY() + ", " + e.getBlockPlaced().getLocation().getBlockZ() + ", " + player.getName());
        }
    }

    public static boolean wasPlacedByPlayer(Block block) {
        if (storedBlocks.contains(block)) {
            return true;
        }
        return false;
    }

    public static void addPlayerPlacedBlock(Block block) {
        storedBlocks.add(block);
    }

    public static void clearBlocks() {
        storedBlocks.clear();
    }

    public static void removePlayerPlacedBlock(Block block) {
        storedBlocks.remove(block);
    }
}
