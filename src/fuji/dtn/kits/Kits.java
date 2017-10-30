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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 9/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Kits {

    static ArrayList<Kit> kits = new ArrayList<>();

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
            try {
                String kitname = iterator.next();
                int price = config.getInt("Kits." + kitname + ".price");
                PotionEffect potionEffect;
                if (!config.getString("Kits." + kitname + ".potionEffect").equalsIgnoreCase("clear")) {
                    potionEffect = PotionEffectType.getByName(config.getString("Kits." + kitname + ".potionEffect")).createEffect(10000000, config.getInt("Kits." + kitname + ".potion.amp"));

                    if (kitname != null) {
                        Kit kit = new Kit(kitname, price, potionEffect);
                    }
                } else {
                    if (kitname != null) {
                        Kit kit = new Kit(kitname, price, null);
                    }
                }


            } catch (Exception ex) {
                console.sendMessage(ChatColor.RED + "Error while loading a kit. Please check to config to make sure it fits the criteria (Criteria: KitName and PotionEffect)");
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
        for (int i = 0; i < getAllRegisteredKits().size(); i++) {
            if (getAllRegisteredKits().get(i).hasPlayer(player)) {
                return getAllRegisteredKits().get(i);
            }
        }
        return null;
    }

    public static String getKitNameByPlayer(Player player) {
        for (int i = 0; i < getAllRegisteredKits().size(); i++) {
            if (getAllRegisteredKits().get(i).hasPlayer(player)) {
                return getAllRegisteredKits().get(i).getName();
            }
        }
        return "Unknown";
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

}
