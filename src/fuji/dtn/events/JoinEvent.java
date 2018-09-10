package fuji.dtn.events;

import fuji.dtn.game.Game;
import fuji.dtn.game.GameState;
import fuji.dtn.game.Lobby;
import fuji.dtn.game.Players;
import fuji.dtn.kits.Kit;
import fuji.dtn.kits.Kits;
import fuji.dtn.main.Main;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import fuji.dtn.titles.Titles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 11/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class JoinEvent implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);
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
        player.setExp(0);
        player.setLevel(0);
        if (GameState.getGameState() == GameState.WAITING || GameState.getGameState() == GameState.STARTING) {

            ArrayList<String> lines = new ArrayList<>();
            lines.add(" ");
            lines.add(ChatColor.RED + "State: " + ChatColor.GRAY + GameState.getGameState().toString());
            lines.add("  ");
            //ScoreboardSidebar.display(player, ChatColor.GOLD + "" + ChatColor.BOLD + "Destroy The Nexus", lines);
            final Titles title = new Titles(player, "", "", new ArrayList<>());
            title.setPrefix(ChatColor.GREEN + "");


            Players.resetPlayers(true);
            Players.addPlayer(player);
            if (Lobby.getLobbyLoc() != null) {
                player.teleport(Lobby.getLobbyLoc());
            } else {
                player.sendMessage(ChatColor.RED + "Lobby has not been set yet. You have been teleported to your last known location.");
            }

            if (GameState.getGameState().equals(GameState.WAITING)) {
                Game.tryStart();
            } else if (GameState.getGameState().equals(GameState.STARTING)) {
                Teams.findOpenTeam(player);
            }

        } else {
            Team red = Teams.getTeamByName("red");
            Team blue = Teams.getTeamByName("blue");

            Teams.findOpenTeam(player);
            Players.addPlayer(player);

            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            for (Player pls : Bukkit.getOnlinePlayers()) {
                pls.showPlayer(player);
            }

            Kit kit = Kits.getDefaultKit();
            Kits.addPlayerToKit(player, kit);
            Kits.setInventory(player, kit);
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (Teams.getTeamFromPlayer(player).equals(red)) {
                        player.teleport(Rotation.getCurrentArena().getRedLocation());
                    } else if (Teams.getTeamFromPlayer(player).equals(blue)) {
                        player.teleport(Rotation.getCurrentArena().getBlueLocation());
                    }
                }
            }.runTaskLater(JavaPlugin.getPlugin(Main.class), 5L);
        }
    }

    private void updateScoreboard(Player player, int delay) {
        //TODO: Move to ScoreboardSidebar.java & add auto update for global server (use onEnable method).
    }


}
