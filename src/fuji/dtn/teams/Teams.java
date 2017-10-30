package fuji.dtn.teams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/19/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Teams {

    public static ArrayList<Team> teams = new ArrayList<>();

    public static void registerTeam(Team team) {
        teams.add(team);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + " # New Team Registry #   " + ChatColor.BLUE + "Name: " + team.getName() + "  " + team.getColor());
    }

    public static void unregisterTeam(Team team) {
        teams.remove(team);
    }

    public static ArrayList<Team> getRegisteredTeams() {
        return teams;
    }

    public static Team getTeamByName(String team) {
        for (int i = 0; i < getRegisteredTeams().size(); i++) {
            if (getRegisteredTeams().get(i).getName().equalsIgnoreCase(team)) {
                return teams.get(i);
            }
        }
        return null;
    }

    static int count = 0;

    public static void balance(ArrayList<UUID> players) {
        Team red = Teams.getTeamByName("red");
        Team blue = Teams.getTeamByName("blue");

        int pcount = players.size();


        for (int i = 0; i < pcount; i++) {

            if (count == 0) {
                count++;
                red.addPlayerUUID(players.get(i));
                Player player = Bukkit.getPlayer(players.get(i));
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You joined " + red.getName().toUpperCase() + " Team.");
                }
            } else if (count == 1) {
                count = 0;
                blue.addPlayerUUID(players.get(i));
                Player player = Bukkit.getPlayer(players.get(i));
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "You joined " + blue.getName().toUpperCase() + " Team.");
                }
            }


        }

        int result = red.size() - blue.size();

        if (result == 0) {
            for (int i = 0; i < count; i++) {
                Player player = Bukkit.getPlayer(players.get(i));
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.GREEN + "Both teams are balanced. Gameplay is 100% fair.");
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                Player player = Bukkit.getPlayer(players.get(i));
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.RED + "Teams are unbalanced by " + result + " player(s).");
                }
            }
        }
    }

    public static ChatColor getColorFromPlayerTeam(Player player) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).hasPlayer(player)) {
                return teams.get(i).getColor();
            }
        }
        return ChatColor.GRAY;
    }

    public static Team getTeamFromPlayer(Player player) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).hasPlayer(player)) {
                return teams.get(i);
            }
        }
        return null;
    }


}
