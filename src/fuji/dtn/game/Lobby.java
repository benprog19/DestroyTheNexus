package fuji.dtn.game;

import fuji.dtn.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 12/12/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Lobby {

    static Location lobbyLoc;

    public Lobby(Location lobbyLoc) {
        setLobbyLoc(lobbyLoc);
    }

    public static void setLobbyLoc(Location location) {
        lobbyLoc = location;

        Main.arenaStorage.get().set("Lobby.world", location.getWorld().getName());
        Main.arenaStorage.get().set("Lobby.x", location.getBlockX() + 0.5);
        Main.arenaStorage.get().set("Lobby.y", location.getBlockY() + 0.5);
        Main.arenaStorage.get().set("Lobby.z", location.getBlockZ() + 0.5);
        Main.arenaStorage.get().set("Lobby.yaw", location.getYaw());
        Main.arenaStorage.get().set("Lobby.pitch", location.getPitch());
        Main.arenaStorage.save();
    }

    public static Location getLobbyLoc() {
        if (lobbyLoc != null) {
            return lobbyLoc;
        } else {
            FileConfiguration section = Main.arenaStorage.get();
            Location location = new Location(Bukkit.getWorld(section.getString("Lobby.world")), section.getDouble("Lobby.x"), section.getDouble("Lobby.y"), section.getDouble("Lobby.z"),
                    section.getInt("Lobby.yaw"), section.getInt("Lobby.pitch"));
            lobbyLoc = location;
            return lobbyLoc;
        }
    }

    public Lobby() {
        lobbyLoc = new Location(Bukkit.getWorld(Main.arenaStorage.get().getString("Lobby.world")), Main.arenaStorage.get().getDouble("Lobby.x"),
                Main.arenaStorage.get().getDouble("Lobby.y"), Main.arenaStorage.get().getDouble("Lobby.z"), Main.arenaStorage.get().getInt("Lobby.yaw"),
                Main.arenaStorage.get().getInt("Lobby.pitch"));
    }

}
