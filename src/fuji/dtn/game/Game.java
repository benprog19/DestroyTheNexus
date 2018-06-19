package fuji.dtn.game;

import com.connorlinfoot.titleapi.TitleAPI;
import fuji.dtn.arena.Arena;
import fuji.dtn.events.BlockPlaceEvent;
import fuji.dtn.events.DeathEvent;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/26/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Game {

    static Arena arena;
    static ArrayList<UUID> players;
    
    public static void init(Arena arena, ArrayList<UUID> players) {
        Game.arena = arena;
        Game.players = players;

        GameState.setGameState(GameState.WAITING);
    }

    public static void beginGame(int time) {
        GameState.setGameState(GameState.STARTING);
        GameTimer timer = new GameTimer(time + 1, players);
        timer.startCountdown();
    }

    public static void endGame(Team winner) {
        players.clear();
        DeathEvent.deadPlayers.clear();
        BlockPlaceEvent.clearBlocks();
        redDestroyed = false;
        blueDestroyed = false;

        for (Player pls : Bukkit.getOnlinePlayers()) {
            if (!Spectators.isSpectator(pls)) {
                Spectators.removePlayer(pls);
            }
            pls.setGameMode(GameMode.SURVIVAL);
            pls.setAllowFlight(true);
            pls.getInventory().clear();
            pls.setHealth(20);
            for (Player pls1 : Bukkit.getOnlinePlayers()) {
                pls.showPlayer(pls1);
            }
        }

        if (Rotation.getCurrentArena() != null) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "                                                                       ");
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "The Winner of " + ChatColor.WHITE
                    + "" + ChatColor.BOLD + Rotation.getCurrentArena().getName() + ChatColor.GOLD + " "
                    + ChatColor.BOLD + "is " + winner.getColor() + "" + ChatColor.BOLD + "TEAM "
                    + winner.getName().toUpperCase() + ChatColor.GOLD + "" + ChatColor.BOLD + "!");
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "                                                                       ");
        }

        GameState.setGameState(GameState.ENDING);
        GameTimer timer = new GameTimer(30, players);

        Team red = Teams.getTeamByName("red");
        Team blue = Teams.getTeamByName("blue");

        Team loser;

        if (winner.equals(red)) {
            loser = blue;
            Location blueloc = Rotation.getCurrentArena().getBlueLocation();
            for (int i = 0; i < 20; i++) {
                blueloc.getWorld().strikeLightning(blueloc);
            }
            blueloc.getWorld().createExplosion(blueloc.getBlockX(), blueloc.getBlockY(), blueloc.getBlockZ(), 50.0F);
            blueloc.getWorld().playSound(blueloc, Sound.ENTITY_ENDERDRAGON_DEATH, 100, 1);


        } else if (winner.equals(blue)) {
            loser = red;
            Location redLoc = Rotation.getCurrentArena().getRedLocation();
            for (int i = 0; i < 20; i++) {
                redLoc.getWorld().strikeLightning(redLoc);
            }
            redLoc.getWorld().createExplosion(redLoc.getBlockX(), redLoc.getBlockY(), redLoc.getBlockZ(), 50.0F);
            redLoc.getWorld().playSound(redLoc, Sound.ENTITY_ENDERDRAGON_DEATH, 100, 1);
        } else {
            loser = null;
            Bukkit.broadcastMessage(ChatColor.RED + "Error while finding loser. Restarting...");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }
        for (Player pls : Bukkit.getOnlinePlayers()) {
            TitleAPI.sendTitle(pls, 5, 100, 5,  winner.getColor() + "" + ChatColor.BOLD + winner.getName().toUpperCase() + " TEAM", ChatColor.BOLD + "WAS VICTORIOUS");
        }

        red.removeAllPlayers();
        blue.removeAllPlayers();

        timer.endingGame(winner, loser);
    }

    public static void tryStart() {
        if (GameState.getGameState().equals(GameState.WAITING)) {
            if (Bukkit.getOnlinePlayers().size() >= 2) {
                Players.getPlayers().clear();

                Team red = Teams.getTeamByName("red");
                Team blue = Teams.getTeamByName("blue");

                red.getPlayers().clear();
                blue.getPlayers().clear();

                for (Player pls : Bukkit.getOnlinePlayers()) {
                    Players.addPlayer(pls);
                    pls.sendMessage(ChatColor.GREEN + "You have been added into the game.");
                }

                try {
                    new Rotation();
                    Game.init(Rotation.getCurrentArena(), Players.getPlayers());
                    Game.beginGame(30);
                } catch (IllegalStateException ex) {
                    Bukkit.broadcastMessage(ChatColor.RED + "There are no available games to play.");
                }


            } else {
                Bukkit.broadcastMessage(ChatColor.RED + "You must wait until more players join before the game begins.");
            }
        }

    }

    public static void forceInGame() {
        GameState.setGameState(GameState.INGAME);
    }

    public static void forceEndGame() {
        GameState.setGameState(GameState.ENDING);
    }

    public static void forceMaitence() {
        GameState.setGameState(GameState.SETTINGUP);
    }

    static boolean redDestroyed = false;
    static boolean blueDestroyed = false;

    public static void lostInvincibility(Team nexusDestroyed) {
        if (nexusDestroyed.getName().equalsIgnoreCase("red")) {
            redDestroyed = true;
        } else if (nexusDestroyed.getName().equalsIgnoreCase("blue")) {
            blueDestroyed = true;
        }
    }

    public static boolean invincible(Team invincible) {
        if (invincible.getName().equalsIgnoreCase("red")) {
            if (redDestroyed) {
                return false;
            } else {
                return true;
            }
        } else if (invincible.getName().equalsIgnoreCase("blue")) {
            if (blueDestroyed) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
