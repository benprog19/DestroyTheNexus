package fuji.dtn.main;

import fuji.dtn.arena.Arena;
import fuji.dtn.arena.Arenas;
import fuji.dtn.arena.ResetArena;
import fuji.dtn.arena.Selection;
import fuji.dtn.commands.DTNCommand;
import fuji.dtn.commands.KitCommand;
import fuji.dtn.events.ChatEvent;
import fuji.dtn.events.DeathEvent;
import fuji.dtn.events.NexusBlockBreakEvent;
import fuji.dtn.game.GameState;
import fuji.dtn.game.Spectators;
import fuji.dtn.kits.Kits;
import fuji.dtn.storage.ArenaStorage;
import fuji.dtn.storage.KitStorage;
import fuji.dtn.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/19/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Main extends JavaPlugin {

    Team red;
    Team blue;

    public static ArenaStorage arenaStorage;
    public static KitStorage kitStorage;

    @Override
    public void onEnable() {

        arenaStorage = new ArenaStorage(this);
        kitStorage = new KitStorage(this);

        getCommand("dtn").setExecutor(new DTNCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getServer().getPluginManager().registerEvents(new Selection(), this);
        getServer().getPluginManager().registerEvents(new Spectators(), this);
        getServer().getPluginManager().registerEvents(new ResetArena(), this);
        getServer().getPluginManager().registerEvents(new NexusBlockBreakEvent(), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);

        red = new Team("Red", ChatColor.RED, new ArrayList<>());
        blue = new Team("Blue", ChatColor.BLUE, new ArrayList<>());

        if (!arenaStorage.getfile().exists()) {
            arenaStorage.create();
            arenaStorage.load();
            arenaStorage.get().set("Arenas", "");
            arenaStorage.save();
        } else {
            arenaStorage.load();
        }

        if (!kitStorage.getfile().exists()) {
            kitStorage.create();
            kitStorage.load();

            kitStorage.get().set("Kits.Standard.name", "Standard");
            kitStorage.get().set("Kits.Standard.price", 0);
            kitStorage.get().set("Kits.Standard.default", true);
            kitStorage.get().set("Kits.Standard.potionEffect", "clear");

            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.material", "IRON_SWORD");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.displayname", "&cStandard Sword");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.slot", 0);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.lore", new ArrayList<String>().add("&7Example Lore"));
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.unbreakable", true);

            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.material", "IRON_PICKAXE");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.displayname", "&cStandard Pickaxe");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.slot", 1);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.lore", new ArrayList<String>().add("&7Example Lore"));
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.unbreakable", true);

            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.material", "IRON_AXE");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.displayname", "&cStandard Axe");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.slot", 2);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.lore", new ArrayList<String>().add("&7Example Lore"));
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.unbreakable", true);

            kitStorage.save();
        } else {
            kitStorage.load();
        }

        new Kits();
        new Arenas();
        GameState.setGameState(GameState.WAITING);
    }

    @Override
    public void onDisable() {
        ArrayList<Arena> arenas = Arenas.getRegisteredArenas();
        for (int i = 0; i < arenas.size(); i++) {
            if (arenas.get(i).isPlayable()) {
                ResetArena.resetArena(arenas.get(i));
                Bukkit.broadcastMessage(ChatColor.GOLD + "# Map Resetting # " + ChatColor.BLUE + "Resetting " + ChatColor.GREEN + arenas.get(i).getName() + ChatColor.BLUE + " from last backup.");
                ResetArena.saveArena(arenas.get(i));
                Bukkit.broadcastMessage(ChatColor.GOLD + "# Map Saving # " + ChatColor.BLUE + "Saving " + ChatColor.GREEN + arenas.get(i).getName() + ChatColor.BLUE + ".");
            }
        }
    }
}
