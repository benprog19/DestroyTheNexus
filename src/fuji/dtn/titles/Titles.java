package fuji.dtn.titles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 5/25/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Titles {

    Player p;
    String prefix;
    String suffix;

    Scoreboard scoreboard;
    Objective obj;
    Team team;

    public Titles(Player player, String prefix, String suffix, ArrayList<String> lines) {
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		if (scoreboard.getObjective("scbrd") != null) {
			obj = scoreboard.getObjective("scbrd");
		} else {
			obj = scoreboard.registerNewObjective("scbrd", "dummy");
		}
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Game" + ChatColor.RED + " " + ChatColor.BOLD + "scbrd");
		if (!(lines.size() > 16) || !(lines.size() < 0)) {
			for (int i = lines.size() - 1; i > -1; i--) {
				Score score = obj.getScore(ChatColor.translateAlternateColorCodes('&', lines.get(i)));
				score.setScore(lines.size() - i);
			}
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}

        if (scoreboard != null) {
            if (p != null) {
                p.setScoreboard(scoreboard); // Displays sidebar.
            }
        }

        try {
            team = scoreboard.registerNewTeam(player.getName());
        } catch (IllegalArgumentException e) {
            team = scoreboard.getTeam(player.getName());
        }


        this.prefix = prefix;
        this.suffix = suffix;

        team.setPrefix(this.prefix);
        team.setSuffix(this.suffix);

        this.p = player;

        if (!team.hasEntry(player.getDisplayName())) {
            System.out.println("Doesn't have player... adding it to the list.");
            team.addEntry(player.getDisplayName()); // Supposedly displays prefix?
            //System.out.println(team.getPlayers().toString());
        }


        System.out.print("Scoreboard: " + scoreboard.getObjective(DisplaySlot.SIDEBAR).getDisplayName() + " : " + scoreboard.getObjective(DisplaySlot.SIDEBAR).getCriteria().toString());
    }

    public Team getTeam() {
        return team;
    }

    public void setPrefix(String newPrefix) {
        this.prefix = newPrefix;
        team.setPrefix(prefix);
    }

    public void setSuffix(String newSuffix) {
        this.suffix = newSuffix;
        team.setSuffix(suffix);
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

}
