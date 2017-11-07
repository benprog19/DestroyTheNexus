package fuji.dtn.arena;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import java.lang.reflect.Field;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 9/3/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class ResetArena implements Listener {


    public static void saveArena(Arena arena) {
        for (Player pls : Bukkit.getOnlinePlayers()) {
            pls.teleport(new Location(Bukkit.getWorlds().get(1), 0, 100, 0));
        }

        Chunk chunkCorner1 = arena.getArenaCorner1().getChunk();
        Chunk chunkCorner2 = arena.getArenaCorner2().getChunk();

        for (int x = chunkCorner1.getX(); x < chunkCorner2.getX(); x++) {
            for (int z = chunkCorner1.getZ(); z < chunkCorner2.getZ(); z++) {
                Chunk chunk = chunkCorner1.getWorld().getChunkAt(x, z);
                chunk.unload(true);
                chunk.load();
            }
        }

        for (Player pls : Bukkit.getOnlinePlayers()) {
            pls.teleport(chunkCorner1.getBlock(8, 64, 8).getLocation());
        }
    }

    public static void resetArena(Arena arena) {
        for (Player pls : Bukkit.getOnlinePlayers()) {
            if (pls != null) {
                pls.teleport(new Location(Bukkit.getWorlds().get(1), 0, 100, 0));
            }
        }

        Chunk chunkCorner1 = arena.getArenaCorner1().getChunk();
        Chunk chunkCorner2 = arena.getArenaCorner2().getChunk();
            for (int x = chunkCorner1.getX(); x < chunkCorner2.getX(); x++) {
                for (int z = chunkCorner1.getZ(); z < chunkCorner2.getZ(); z++) {
                    Chunk chunk = chunkCorner1.getWorld().getChunkAt(x, z);
                    chunk.unload(false);
                    chunk.load();
                }
            }
        for (Player pls : Bukkit.getOnlinePlayers()) {
            if (pls != null) {
                pls.teleport(new Location(Bukkit.getWorlds().get(0), 0, 100, 0));
            }
        }


    }

    @EventHandler
    public void onSave(WorldSaveEvent event) {
        for(Chunk c : event.getWorld().getLoadedChunks()) { // iterate through any chunks (in this world) that you don't want saved (i.e. any chunks that should be reset at some point)

            net.minecraft.server.v1_12_R1.Chunk chunk = ((CraftChunk) c).getHandle(); // grab the 'Chunk' handle object for this chunk (aka the internal, default minecraft chunk object - not the bukkit one)
            try {
                // the next six lines of code disable the 'r' and 'q' fields to false, using reflection. Both fields are used to decide whether or not saving the chunk should override the autosave settings. Without changing these fields, the server will save the chunks even if autosave is disabled.
                Field field = chunk.getClass().getDeclaredField("t");
                field.setAccessible(true);
                field.set(chunk, false);

                field = chunk.getClass().getDeclaredField("s");
                field.setAccessible(true);
                field.set(chunk, false);
            } catch (IllegalAccessException | NoSuchFieldException ex) {
                ex.printStackTrace();
            }
        }
    }
}
