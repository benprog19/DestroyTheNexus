package fuji.dtn.teams;

import org.bukkit.ChatColor;
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
        players.add(player.getUniqueId());
    }

    public void addPlayerUUID(UUID uuid) {
        players.add(uuid);
    }

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

    public void setPercent(int newPercent) {
        percent = newPercent;
    }

    public int subtractPercent(int sub) {
        return percent - sub;
    }

    public int addPercent(int add) {
        return percent + add;
    }

    public int getPercent() {
        return percent;
    }

}
