package fuji.dtn.kits;

import fuji.dtn.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 9/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Kits {

    static ArrayList<Kit> kits = new ArrayList<>();

    static ConcurrentHashMap<UUID, Kit> playerToKit = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String, ItemStack> itemIDs = new ConcurrentHashMap<>();
    static ConcurrentHashMap<Integer, ItemStack> item = new ConcurrentHashMap<>();
    static ConcurrentHashMap<Kit, PotionEffect> potionEffects = new ConcurrentHashMap<>();


    public Kits() {
        kits.clear();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(ChatColor.GOLD + " # Kit Loading #");

        FileConfiguration config = Main.kitStorage.get();

        console.sendMessage(ChatColor.BLUE + "Found " + kits.size() + " in software.");

        Set<String> keys = config.getConfigurationSection("Kits").getKeys(false);
        Iterator<String> iterator = keys.iterator();


        console.sendMessage(ChatColor.BLUE + "Found " + keys.size() + " in file.");
        console.sendMessage(ChatColor.GREEN + "Loading kits from file...");

        while (iterator.hasNext()) {
            String kitname = iterator.next();
            int price = config.getInt("Kits." + kitname + ".price");
            PotionEffect potionEffect;
            try {
                if (!config.getString("Kits." + kitname + ".potionEffect").equalsIgnoreCase("clear") || config.get("Kits." + kitname + ".potionEffect") != null) {
                    potionEffect = PotionEffectType.getByName(config.getString("Kits." + kitname + ".potionEffect")).createEffect(10000000, calculatePotionAmp(config, kitname));
                    if (kitname != null) {
                        Kit kit = new Kit(kitname, price, potionEffect, config.getBoolean("Kits." + kitname + ".default"));
                        potionEffects.put(kit, potionEffect);
                    }
                } else {
                    if (kitname != null) {
                        Kit kit = new Kit(kitname, price, null, config.getBoolean("Kits." + kitname + ".default"));
                        potionEffects.put(kit, null);
                    }
                }
            } catch (NullPointerException ex){
                new Kit(kitname, price, null, config.getBoolean("Kits." + kitname + ".default"));
            }


        }

        console.sendMessage(ChatColor.GREEN + "Total Loaded Kits: " + kits.size());

    }

    public static void registerKit(Kit kit) {
        kits.add(kit);

        ConfigurationSection inventory = Main.kitStorage.get().getConfigurationSection("Kits." + kit.getName() + ".Inventory");

        Set<String> itemsConfig = Main.kitStorage.get().getConfigurationSection("Kits." + kit.getName() + ".Inventory").getKeys(false);
        Iterator<String> itemIterator = itemsConfig.iterator();

        while (itemIterator.hasNext()) {
            String itemID = itemIterator.next();

            ConfigurationSection itemSection = inventory.getConfigurationSection("." + itemID);
            ItemStack itemStack = createItemStack(itemSection);
            item.put(itemSection.getInt(".slot"), itemStack);
            itemIDs.put(itemID, itemStack);
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + " # New Kit Registry #   " + ChatColor.BLUE + "Name: " + kit.getName() + "  " + kit.getPrice());
    }

    public static void unregisterKit(Kit kit) {
        kits.remove(kit);
    }

    public static ArrayList<Kit> getAllRegisteredKits() {
        return kits;
    }

    public static Kit getKitByName(String kitname) {
        for (int i = 0; i < getAllRegisteredKits().size(); i++) {
            if (getAllRegisteredKits().get(i).getName().equalsIgnoreCase(kitname)) {
                return getAllRegisteredKits().get(i);
            }
        }
        return null;
    }

    public static Kit getKitByPlayer(Player player) {
        return playerToKit.get(player.getUniqueId());
    }

    public static String getKitNameByPlayer(Player player) {
        if (getKitByPlayer(player) != null) {
            return getKitByPlayer(player).getName();
        }

        return "Unknown";
    }

    public static void addPlayerToKit(Player player, Kit kit) {
        if (kit != null) {
            playerToKit.put(player.getUniqueId(), kit);
            player.sendMessage(ChatColor.GOLD + "You have selected the " + ChatColor.RED + kit.getName() + ChatColor.GOLD + " kit.");
        }
    }

    public static void removePlayerFromKit(Player player) {
        playerToKit.remove(player.getUniqueId());
    }

    public static ItemStack createItemStack(ConfigurationSection section) {
        ItemStack itemStack = new ItemStack(Material.valueOf(section.getString(".material")), section.getInt(".amount"), (byte) section.getInt(".data"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(section.getBoolean(".unbreakable"));
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString(".displayname")));
        ArrayList<String> lore = new ArrayList<>();
        for (int i = 0; i < section.getStringList(".lore").size(); i++) {
            lore.add(ChatColor.translateAlternateColorCodes('&', section.getStringList(".lore").get(i)));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static Kit getDefaultKit() {
        for (int i = 0; i < getAllRegisteredKits().size(); i++) {
            if (getAllRegisteredKits().get(i).isDefault()) {
                return getAllRegisteredKits().get(i);
            }
        }

        Random random = new Random();
        int rand = random.nextInt(getAllRegisteredKits().size());
        return getAllRegisteredKits().get(rand);
    }

    private int calculatePotionAmp(FileConfiguration config, String kitname) {
        if (config.get("Kits." + kitname + ".potionEffect.amp") != null) {
            return config.getInt("Kits." + kitname + ".potionEffect.amp");
        } else {
            return 0;
        }
    }

}
