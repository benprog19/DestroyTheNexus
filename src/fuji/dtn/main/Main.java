package fuji.dtn.main;

import fuji.dtn.arena.Arenas;
import fuji.dtn.arena.ResetArena;
import fuji.dtn.arena.Selection;
import fuji.dtn.commands.DTNCommand;
import fuji.dtn.commands.KitCommand;
import fuji.dtn.events.*;
import fuji.dtn.game.GameState;
import fuji.dtn.game.Lobby;
import fuji.dtn.game.Players;
import fuji.dtn.game.Spectators;
import fuji.dtn.kits.Kits;
import fuji.dtn.storage.ArenaStorage;
import fuji.dtn.storage.KitStorage;
import fuji.dtn.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
        getServer().getPluginManager().registerEvents(new QuitEvent(), this);
        getServer().getPluginManager().registerEvents(new PvPEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new HungerEvent(), this);
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        getServer().getPluginManager().registerEvents(new ItemPickUpEvent(), this);
        getServer().getPluginManager().registerEvents(new CraftEvent(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceEvent(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);

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

            kitStorage.get().options().header("Please refer to https://i.gyazo.com/9d52e1fd4dc14790ec66eab4a9aee00e.png for slot numbers.");
            kitStorage.get().set("Kits.Standard.name", "Standard");
            kitStorage.get().set("Kits.Standard.price", 0);
            kitStorage.get().set("Kits.Standard.default", true);
            kitStorage.get().set("Kits.Standard.coloredArmor", true);
            kitStorage.get().set("Kits.Standard.potionEffect", "clear");

            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.material", "IRON_SWORD");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.displayname", "&cStandard Sword");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.slot", 0);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.lore", new ArrayList<String>().add("&7Default Iron Sword"));
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem.unbreakable", true);

            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.material", "IRON_PICKAXE");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.displayname", "&cStandard Pickaxe");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.slot", 1);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.lore", new ArrayList<String>().add("&7Default Iron Pickaxe"));
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem1.unbreakable", true);

            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.material", "IRON_AXE");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.displayname", "&cStandard Axe");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.slot", 2);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.lore", new ArrayList<String>().add("&7Default Iron Axe"));
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem2.unbreakable", true);

            kitStorage.get().set("Kits.Standard.Inventory.exampleItem3.material", "WOOD");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem3.displayname", "&cWood");
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem3.slot", 8);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem3.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem3.amount", 64);
            kitStorage.get().set("Kits.Standard.Inventory.exampleItem3.lore", new ArrayList<String>().add("&7Default Wood Stack"));

            kitStorage.get().set("Kits.Standard.Inventory.protection1.material", "LEATHER_HELMET");
            kitStorage.get().set("Kits.Standard.Inventory.protection1.armorType", "helmet");
            kitStorage.get().set("Kits.Standard.Inventory.protection1.displayname", "&cHelmet");
            kitStorage.get().set("Kits.Standard.Inventory.protection1.slot", 5);
            kitStorage.get().set("Kits.Standard.Inventory.protection1.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.protection1.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.protection1.lore", new ArrayList<String>().add("&7Protection"));
            kitStorage.get().set("Kits.Standard.Inventory.protection1.unbreakable", true);

            kitStorage.get().set("Kits.Standard.Inventory.protection2.material", "LEATHER_CHESTPLATE");
            kitStorage.get().set("Kits.Standard.Inventory.protection2.armorType", "chestplate");
            kitStorage.get().set("Kits.Standard.Inventory.protection2.displayname", "&cChestplate");
            kitStorage.get().set("Kits.Standard.Inventory.protection2.slot", 6);
            kitStorage.get().set("Kits.Standard.Inventory.protection2.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.protection2.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.protection2.lore", new ArrayList<String>().add("&7Protection"));
            kitStorage.get().set("Kits.Standard.Inventory.protection2.unbreakable", true);

            kitStorage.get().set("Kits.Standard.Inventory.protection3.material", "LEATHER_LEGGINGS");
            kitStorage.get().set("Kits.Standard.Inventory.protection3.armorType", "leggings");
            kitStorage.get().set("Kits.Standard.Inventory.protection3.displayname", "&cLeggings");
            kitStorage.get().set("Kits.Standard.Inventory.protection3.slot", 7);
            kitStorage.get().set("Kits.Standard.Inventory.protection3.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.protection3.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.protection3.lore", new ArrayList<String>().add("&7Protection"));
            kitStorage.get().set("Kits.Standard.Inventory.protection3.unbreakable", true);

            kitStorage.get().set("Kits.Standard.Inventory.protection4.material", "LEATHER_BOOTS");
            kitStorage.get().set("Kits.Standard.Inventory.protection4.armorType", "boots");
            kitStorage.get().set("Kits.Standard.Inventory.protection4.displayname", "&cBoots");
            kitStorage.get().set("Kits.Standard.Inventory.protection4.slot", 8);
            kitStorage.get().set("Kits.Standard.Inventory.protection4.data", 0);
            kitStorage.get().set("Kits.Standard.Inventory.protection4.amount", 1);
            kitStorage.get().set("Kits.Standard.Inventory.protection4.lore", new ArrayList<String>().add("&7Protection"));
            kitStorage.get().set("Kits.Standard.Inventory.protection4.unbreakable", true);

            kitStorage.save();
        } else {
            kitStorage.load();
        }

        new Kits();
        new Arenas();
        new Lobby();
        GameState.setGameState(GameState.WAITING);

        for (Player pls : Bukkit.getOnlinePlayers()) {
            if (Lobby.getLobbyLoc().getWorld().getName() != null) {
                pls.teleport(Lobby.getLobbyLoc());
                System.out.print(Lobby.getLobbyLoc().toString());
            } else {
                pls.sendMessage(ChatColor.RED + "Lobby location is invalid. Teleporting to your last known location.");
            }
            Players.resetPlayers(true);

        }
    }

    @Override
    public void onDisable() {
        Arenas.resetAll();
    }
}
