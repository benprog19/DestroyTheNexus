package fuji.dtn.game;

import fuji.dtn.arena.Arena;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/26/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Players {

    static ArrayList<UUID> players = new ArrayList<>();

    public static void addPlayer(Player player) {
        if (!isPlayer(player)) {
            players.add(player.getUniqueId());
        }
    }

    public static void removePlayer(Player player) {
        if (isPlayer(player)) {
            players.remove(player.getUniqueId());
        }
    }

    public static boolean isPlayer(Player player) {
        if (players.contains(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    public static ArrayList<UUID> getPlayers() {
        return players;
    }

    public static void teleportPlayerToTeams(Player singlePlayer, boolean allPlayers) {
        Team red = Teams.getTeamByName("red");
        Team blue = Teams.getTeamByName("blue");
        Arena arena = Rotation.getCurrentArena();
        if (red != null && blue != null && arena != null) {
            if (allPlayers) {
                for (int i = 0; i < getPlayers().size(); i++) {
                    Player player = Bukkit.getPlayer(getPlayers().get(i));
                    if (red.hasPlayer(player)) {
                        player.teleport(new Location(arena.getRedLocation().getWorld(), arena.getRedLocation().getX(), arena.getRedLocation().getY(), arena.getRedLocation().getZ(), arena.getRedLocation().getYaw(), arena.getRedLocation().getPitch()));
                    } else if (blue.hasPlayer(player)) {
                        player.teleport(new Location(arena.getBlueLocation().getWorld(), arena.getBlueLocation().getX(), arena.getBlueLocation().getY(), arena.getBlueLocation().getZ(), arena.getBlueLocation().getYaw(), arena.getBlueLocation().getPitch()));
                    } else {
                        Teams.findOpenTeam(player);
                        if (red.hasPlayer(player)) {
                            player.teleport(arena.getRedLocation());
                        } else if (blue.hasPlayer(player)) {
                            player.teleport(arena.getBlueLocation());
                        }
                    }
                }
            } else {
                if (red.hasPlayer(singlePlayer)) {
                    singlePlayer.teleport(new Location(arena.getRedLocation().getWorld(), arena.getRedLocation().getX(), arena.getRedLocation().getY(), arena.getRedLocation().getZ(), arena.getRedLocation().getYaw(), arena.getRedLocation().getPitch()));
                } else if (blue.hasPlayer(singlePlayer)) {
                    singlePlayer.teleport(new Location(arena.getBlueLocation().getWorld(), arena.getBlueLocation().getX(), arena.getBlueLocation().getY(), arena.getBlueLocation().getZ(), arena.getBlueLocation().getYaw(), arena.getBlueLocation().getPitch()));
                } else {
                    Teams.findOpenTeam(singlePlayer);
                    if (red.hasPlayer(singlePlayer)) {
                        singlePlayer.teleport(arena.getRedLocation());
                    } else if (blue.hasPlayer(singlePlayer)) {
                        singlePlayer.teleport(arena.getBlueLocation());
                    }
                }

            }
        }
    }

    public static void fixPlayerSetup(ArrayList<UUID> players) {
        if (Players.getPlayers().size() != players.size()) {
            Players.getPlayers().clear();
            players.clear();
            Iterator<? extends Player> playersIter = Bukkit.getOnlinePlayers().iterator();
            while (playersIter.hasNext()) {
                Player player = playersIter.next();
                Players.addPlayer(player);
                players.add(player.getUniqueId());
            }
            Bukkit.broadcastMessage(ChatColor.GREEN + "All players are set and ready to go.");
            Teams.balance(players);
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "Balancing Teams...");
        Teams.balance(players);
    }

    public static void setupPlayers() {
        teleportPlayerToTeams(null, true);
    }

    public static void resetPlayers(boolean forLobby) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
            for (Player pls : Bukkit.getOnlinePlayers()) {
                pls.showPlayer(player);
            }

            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            if (forLobby) {
                ItemStack itemStack = new ItemStack(Material.FEATHER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Select A Kit");
                itemStack.setItemMeta(itemMeta);
                player.getInventory().setItem(0, itemStack);
            }
        }
    }

}
