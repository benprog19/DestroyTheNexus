package fuji.dtn.game;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/27/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public enum GameState {

    WAITING,
    STARTING,
    INGAME,
    ENDING,
    SETTINGUP;

    static GameState currentState;

    public static void setGameState(GameState gameState) {
        currentState = gameState;
    }

    public static GameState getGameState() {
        if (currentState != null) {
            return currentState;
        }
        return null;
    }

}
