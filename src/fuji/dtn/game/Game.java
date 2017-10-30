package fuji.dtn.game;

import fuji.dtn.arena.Arena;

import java.util.ArrayList;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/26/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Game {

    Arena arena;
    ArrayList<UUID> players;

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
