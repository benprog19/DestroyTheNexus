package fuji.dtn.arena;

import fuji.dtn.game.Lobby;
import fuji.dtn.main.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.*;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/19/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Arenas {

    public Arenas() {
        arenas.clear();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(ChatColor.GOLD + " # Arena Loading #");

        FileConfiguration config = Main.arenaStorage.get();

        console.sendMessage(ChatColor.BLUE + "Found " + arenas.size() + " in software.");

        Set<String> keys = config.getConfigurationSection("Arenas").getKeys(false);
        Iterator<String> iterator = keys.iterator();

        console.sendMessage(ChatColor.BLUE + "Found " + keys.size() + " in file.");
        console.sendMessage(ChatColor.GREEN + "Loading arenas from file...");

        while(iterator.hasNext()) {
            String iter = iterator.next();

            Arena arena = Arena.createArena(config.getConfigurationSection("Arenas." + iter));

            console.sendMessage(ChatColor.BLUE + "Loaded " + arena.getName() + "...");
        }
        console.sendMessage(ChatColor.GREEN + "Total Loaded Arenas: " + arenas.size());

    }

    public static ArrayList<Arena> arenas = new ArrayList<>();

    public static void registerArena(Arena arena) {
        arenas.add(arena);

        Main.arenaStorage.get().set("Arenas." + arena.getName() + ".name", arena.getName());
        Main.arenaStorage.get().set("Arenas." + arena.getName() + ".creator", arena.getCreator());
        Main.arenaStorage.get().set("Arenas." + arena.getName() + ".minPlayers", arena.getMinPlayers());
        Main.arenaStorage.get().set("Arenas." + arena.getName() + ".time", arena.getTime());

        if (arena.getRedLocation() != null) {
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".redLocation.x", arena.getRedLocation().getX());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".redLocation.y", arena.getRedLocation().getY());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".redLocation.z", arena.getRedLocation().getZ());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".redLocation.yaw", arena.getRedLocation().getYaw());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".redLocation.pitch", arena.getRedLocation().getPitch());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".redLocation.world", arena.getRedLocation().getWorld().getName());
        }

        if (arena.getBlueLocation() != null) {
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".blueLocation.x", arena.getBlueLocation().getX());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".blueLocation.y", arena.getBlueLocation().getY());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".blueLocation.z", arena.getBlueLocation().getZ());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".blueLocation.yaw", arena.getBlueLocation().getYaw());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".blueLocation.pitch", arena.getBlueLocation().getPitch());
            Main.arenaStorage.get().set("Arenas." + arena.getName() + ".blueLocation.world", arena.getBlueLocation().getWorld().getName());
        }

        Main.arenaStorage.save();

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + " # New Arena Registry #   " + ChatColor.BLUE + "Name: " + arena.getName() + "  " + arena.getCreator());
    }

    public static void unregisterArenas(Arena arena) {
        arenas.remove(arena);
        Main.arenaStorage.get().set("Arenas." + arena.getName(), null);
        Main.arenaStorage.save();
    }

    public static ArrayList<Arena> getRegisteredArenas() {
        return arenas;
    }

    public static boolean isRegistered(String arena) {
        if (Main.arenaStorage.get().getConfigurationSection("Arenas." + arena) != null) {
            return true;
        } else {
            for (int i = 0; i < arenas.size(); i++) {
                if (arenas.get(i).getName().equalsIgnoreCase(arena)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Arena getArenaByName(String arena) {
        if (isRegistered(arena)) {
            for (int i = 0; i < arenas.size(); i++) {
                if (arenas.get(i).getName().equalsIgnoreCase(arena)) {
                    return arenas.get(i);
                }
            }
        }
        return null;
    }

    public static void resetAll() {
        Bukkit.broadcastMessage(ChatColor.RED + "Resetting all Arenas...");

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());

        for (Player pls : Bukkit.getOnlinePlayers()) {
            pls.teleport(new Location(Bukkit.getWorlds().get(1), 0.5, 100.0, 0.5));
        }
        ArrayList<Arena> arenas = getRegisteredArenas();
        for (int i = 0; i < arenas.size(); i++) {
            if (arenas.get(i).isPlayable()) {
                ResetArena.resetAndSaveArena(arenas.get(i), false);
                Bukkit.broadcastMessage(ChatColor.GOLD + "# " + arenas.get(i).getName() + " # " + ChatColor.BLUE + "Reset and Saved.");
            }
        }
        for (Player pls : Bukkit.getOnlinePlayers()) {
            pls.teleport(Lobby.getLobbyLoc());
        }
        Date date1 = new Date();
        Timestamp timestamp1 = new Timestamp(date1.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(timestamp1.getTime());

        long time = calendar1.getTimeInMillis() - calendar.getTimeInMillis();

        Bukkit.broadcastMessage(ChatColor.GREEN + "Finished resetting all arenas. (" + time + "ms)");
    }

}
