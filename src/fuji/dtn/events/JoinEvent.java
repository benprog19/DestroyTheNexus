package fuji.dtn.events;

import fuji.dtn.game.Game;
import fuji.dtn.game.GameState;
import fuji.dtn.game.Lobby;
import fuji.dtn.game.Players;
import fuji.dtn.kits.Kit;
import fuji.dtn.kits.Kits;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;

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

        if (GameState.getGameState() == GameState.WAITING || GameState.getGameState() == GameState.STARTING) {

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
            if (Teams.getTeamFromPlayer(player).equals(red)) {
                player.teleport(Rotation.getCurrentArena().getRedLocation());
            } else if (Teams.getTeamFromPlayer(player).equals(blue)) {
                player.teleport(Rotation.getCurrentArena().getBlueLocation());
            }
        }
    }

    private void updateScoreboard(Player player, int delay) {
        //TODO: Move to ScoreboardSidebar.java & add auto update for global server (use onEnable method).
    }


}
