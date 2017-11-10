package fuji.dtn.game;

import fuji.dtn.arena.Arena;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    public Game(Arena arena, ArrayList<UUID> players) {
        this.arena = arena;
        this.players = players;

        GameState.setGameState(GameState.WAITING);
    }

    public void beginGame() {
        GameState.setGameState(GameState.STARTING);
        GameTimer timer = new GameTimer(1, players);
        timer.startCountdown();
    }

    public static void endGame(Team winner) {
        players.clear();
        for (Player pls : Bukkit.getOnlinePlayers()) {
            if (!Spectators.isSpectator(pls)) {
                players.add(pls.getUniqueId());
                Players.addPlayer(pls);
            }
        }

        if (Rotation.getCurrentArena() != null) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "The Winner of " + ChatColor.WHITE
                    + "" + ChatColor.BOLD + Rotation.getCurrentArena().getName() + ChatColor.GOLD + " "
                    + ChatColor.BOLD + "is " + winner.getColor() + "" + ChatColor.BOLD + "TEAM "
                    + winner.getName().toUpperCase() + ChatColor.GOLD + "" + ChatColor.BOLD + "!");
        }

        GameState.setGameState(GameState.ENDING);
        GameTimer timer = new GameTimer(30, players);

        Team red = Teams.getTeamByName("red");
        Team blue = Teams.getTeamByName("blue");

        Team loser;

        if (winner.equals(red)) {
            loser = blue;
        } else if (winner.equals(blue)) {
            loser = red;
        } else {
            loser = null;
            Bukkit.broadcastMessage(ChatColor.RED + "Error while finding loser. Restarting...");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }
        timer.endingGame(winner, loser);
    }

    public void forceInGame() {
        GameState.setGameState(GameState.INGAME);
    }

    public void forceEndGame() {
        GameState.setGameState(GameState.ENDING);
    }

    public void forceMaitence() {
        GameState.setGameState(GameState.SETTINGUP);
    }
}
