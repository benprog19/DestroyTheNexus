package fuji.dtn.teams;

import com.connorlinfoot.titleapi.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/19/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Team {

    String name;
    ChatColor color;
    ArrayList<UUID> players = new ArrayList<>();

    int percent;

    public Team(String name, ChatColor color, ArrayList<UUID> players) {
        this.name = name;
        this.color = color;
        this.players = players;
        this.percent = 100;

        Teams.registerTeam(this);
    }

    public void addPlayer(Player player) {
        if (!players.contains
                (player.
                        getUniqueId())) {
            players.add(player.getUniqueId());
            player.sendMessage(ChatColor.GOLD + "You joined the " + color + "" + ChatColor.BOLD + name.toUpperCase() + " TEAM" + ChatColor.GOLD + ".");
            TitleAPI.sendTitle(player, 0, 100, 20, "", ChatColor.WHITE + "You are on the " + color + name.toUpperCase() + " TEAM" + ChatColor.WHITE + ".");
        }
    }

    public void bc(String message) {
        for (int i = 0; i < players.size(); i++) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(players.get(i));
            if (player.isOnline()) {
                ((Player) player).sendMessage(message);
            }
        }
    }

    public void removeAllPlayers() {
        players.clear();
    }

    public void addPlayerUUID(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        addPlayer(player);
    }

    @Deprecated
    public void changeColor(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean hasPlayer(Player player) {
        if (players.contains(player.getUniqueId())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasPlayerUUID(UUID uuid) {
        if (players.contains(uuid)) {
            return true;
        } else {
            return false;
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public void removePlayerUUID(UUID uuid) {
        players.remove(uuid);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return players.size();
    }

    @Deprecated
    public void setPercent(int newPercent) {
        percent = newPercent;
    }

    @Deprecated
    public int subtractPercent(int sub) {
        return percent - sub;
    }

    @Deprecated
    public int addPercent(int add) {
        return percent + add;
    }

    @Deprecated
    public int getPercent() {
        return percent;
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }

}
