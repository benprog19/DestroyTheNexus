package fuji.dtn.arena;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/24/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class SelectionMode {

    static HashMap<UUID, Arena> players = new HashMap<>();

    public static void addPlayer(Player player, Arena arena) {
        if (!hasPlayer(player)) {
            players.put(player.getUniqueId(), arena);
        }
    }

    public static void removePlayer(Player player) {
        if (hasPlayer(player)) {
            players.remove(player.getUniqueId());
        }
    }

    public static boolean hasPlayer(Player player) {
        if (players.containsKey(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    public static Arena getArena(Player player) {
        return players.get(player.getUniqueId());
    }

}
