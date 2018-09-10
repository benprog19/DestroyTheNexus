package fuji.dtn.rotation;

import fuji.dtn.arena.Arena;
import fuji.dtn.arena.Arenas;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Random;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/23/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Rotation {

    static Arena currentArena;

    public Rotation() {
        ArrayList<Arena> arenas = Arenas.getRegisteredArenas();
        ArrayList<Arena> possibleChoices = new ArrayList<>();
        int amount = arenas.size();
        for (int i = 0; i < amount; i++) {
            Arena arena = arenas.get(i);
            if (arena.isPlayable() && (Bukkit.getOnlinePlayers().size() >= arena.getMinPlayers())) {
                possibleChoices.add(arena);
            }
        }

        if (!possibleChoices.isEmpty()) {
            Random random = new Random();
            int rand = random.nextInt(possibleChoices.size());
            Arena arena = possibleChoices.get(rand);
            if (arena.isPlayable()) {
                currentArena = arena;
                Bukkit.broadcastMessage(ChatColor.GREEN + "There is a total of " + possibleChoices.size() + " arena(s) to play.");
                Bukkit.broadcastMessage(ChatColor.GOLD + "Rotation set to " + ChatColor.RED + getCurrentArena().getName() + ChatColor.GOLD + " created by " + ChatColor.RED + getCurrentArena().getCreator());
            }
        } else {
            currentArena = null;
            throw new IllegalStateException();
        }
    }

    public static Arena getCurrentArena() {
        if (currentArena != null) {
            return currentArena;
        }
        return null;
    }

    public static void nullifyArena() {
        currentArena = null;
    }

    public static void setNewArena(String arenaName) {
        if (Arenas.isRegistered(arenaName)) {
            Arena arena = Arenas.getArenaByName(arenaName);
            currentArena = arena;
            Bukkit.broadcastMessage(ChatColor.GOLD + "Rotation set to " + ChatColor.RED + getCurrentArena().getName() + ChatColor.GOLD + " created by " + ChatColor.RED + getCurrentArena().getCreator());
        }

    }


}
