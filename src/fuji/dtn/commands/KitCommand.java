package fuji.dtn.commands;

import fuji.dtn.kits.Kit;
import fuji.dtn.kits.Kits;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 9/30/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class KitCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("kit")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.GOLD + "/kit select <kit>");
                player.sendMessage(ChatColor.GOLD + "/kit <kit>");
                player.sendMessage(ChatColor.GOLD + "/kit list");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("select")) {
                    player.sendMessage(ChatColor.RED + "Please specify which kit you want to choose.");
                } else if (args[0].equalsIgnoreCase("list")) {
                    ArrayList<Kit> kits = Kits.getAllRegisteredKits();
                    player.sendMessage(ChatColor.GOLD + "Available Kits:");
                    if (kits.size() > 0) {
                        for (int i = 0; i < kits.size(); i++) {
                            player.sendMessage(ChatColor.RED + " " + kits.get(i).getName());
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + " No kits available.");
                    }
                } else if (args[0] != null) {
                    String kitname = args[0];
                    Kit kit = Kits.getKitByName(kitname);
                    if (kit != null) {
                        Kits.addPlayerToKit(player, kit);
                    } else {
                        player.sendMessage(ChatColor.RED + "That kit does not exist.");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("select")) {
                    String kitname = args[1];
                    Kit kit = Kits.getKitByName(kitname);
                    if (kit != null) {
                        Kits.addPlayerToKit(player, kit);
                    } else {
                        player.sendMessage(ChatColor.RED + "That kit does not exist.");
                    }
                }
            }
        }
        return true;
    }
}
