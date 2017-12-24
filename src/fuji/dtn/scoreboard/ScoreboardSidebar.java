package fuji.dtn.scoreboard;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 12/22/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class ScoreboardSidebar {

    static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    static Objective obj;

    public static void display(Player player, String title, ArrayList<String> lines) {
        if (scoreboard != null) {
            obj = scoreboard.registerNewObjective("dum", "dummy");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));

            if (!(lines.size() > 16) || !(lines.size() < 0)) {
                for (int i = lines.size() - 1; i > -1; i--) {
                    Score score = obj.getScore(ChatColor.translateAlternateColorCodes('&', lines.get(i)));
                    score.setScore(lines.size() - i);
                }
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
    }


    public static void setScoreboard(Player player) {
        if (player != null) {
            if (scoreboard != null) {
                player.setScoreboard(scoreboard);
            }
        }
    }

    public static void resetBoard(Player p) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        p.setScoreboard(scoreboard);
    }

    public static void removeAll() {
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
    }

    public static void removeLine(int line) {
        for (String entry : scoreboard.getEntries()) {
            if (obj.getScore(entry).getScore() == line) {
                scoreboard.resetScores(entry);
                break;
            }
        }
    }

    public static String getTitle() {
        return obj.getDisplayName();
    }

    public static void setTitle(String name) {
        obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
    }

    public static void set(int row, String text) {
        Validate.isTrue(16 > row, "Row can't be higher than 16");
        if (text.length() > 32) {
            text = text.substring(0, 32);
        }

        for (String entry : scoreboard.getEntries()) {
            if (obj.getScore(entry).getScore() == row) {
                scoreboard.resetScores(entry);
                break;
            }
        }

        obj.getScore(ChatColor.translateAlternateColorCodes('&', text)).setScore(row);
    }
}