package fuji.dtn.kits;

import fuji.dtn.main.Main;
import fuji.dtn.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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

    /*
    public static void setInventory(Player player, Kit kit) {
        player.getInventory().clear();
        player.updateInventory();
        String name = kit.getName();
        System.out.print("Kit Inv: " + kit.getName());
        ConcurrentHashMap<Integer, ItemStack> itemStackHashMap = item;
        for (Map.Entry<Integer, ItemStack> entry : itemStackHashMap.entrySet()) {
            if (entry.getValue().getType().equals(Material.LEATHER_HELMET) || entry.getValue().getType().equals(Material.LEATHER_CHESTPLATE)
                    || entry.getValue().getType().equals(Material.LEATHER_LEGGINGS) || entry.getValue().getType().equals(Material.LEATHER_BOOTS)) {
                if (Main.kitStorage.get().getBoolean("Kits." + name + ".coloredArmor")) {
                    KitArmorSet kitArmorSet = new KitArmorSet(name);

                    ItemStack itemStack;
                    if (entry.getValue().getType().equals(Material.LEATHER_HELMET)) {
                        //System.out.print("Helmet");
                        itemStack = kitArmorSet.getHelmet();
                        ItemStack itemStackEdit = setColor(itemStack, player);
                        player.getInventory().setHelmet(itemStackEdit);
                    } else if (entry.getValue().getType().equals(Material.LEATHER_CHESTPLATE)) {
                        //System.out.print("Chest");
                        itemStack = kitArmorSet.getChestplate();
                        ItemStack itemStackEdit = setColor(itemStack, player);
                        player.getInventory().setChestplate(itemStackEdit);
                    } else if (entry.getValue().getType().equals(Material.LEATHER_LEGGINGS)) {
                        //System.out.print("Legs");
                        itemStack = kitArmorSet.getLeggings();
                        ItemStack itemStackEdit = setColor(itemStack, player);
                        player.getInventory().setLeggings(itemStackEdit);
                    } else if (entry.getValue().getType().equals(Material.LEATHER_BOOTS)) {
                        //System.out.print("Boots");
                        itemStack = kitArmorSet.getBoots();
                        ItemStack itemStackEdit = setColor(itemStack, player);
                        player.getInventory().setBoots(itemStackEdit);
                    }
                }
            } else {
               // System.out.print("Slot Num: " + entry.getKey());
                ItemStack itemStack = entry.getValue();
              //  System.out.print("Slot DisplayName: " + itemStack.getItemMeta().getDisplayName());
                player.getInventory().setItem(entry.getKey(), itemStack);
            }
            player.updateInventory();
        }
        kit.setPotionEffect(player);
    }
    */

    public static void setInventory(Player player, Kit kit) {
        player.getInventory().clear();
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (!potionEffect.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
                player.removePotionEffect(potionEffect.getType());
            }
        }
        player.updateInventory();
        String name = kit.getName();
        System.out.print("Kit Inv: " + kit.getName());

        ConfigurationSection pathInventory = Main.kitStorage.get().getConfigurationSection("Kits." + kit.getName() + ".Inventory");

        Set<String> set = pathInventory.getKeys(false);
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
            String configItemName = iterator.next();
            ItemStack itemStack = createItemStack(pathInventory.getConfigurationSection("." + configItemName));
            System.out.print("ItemStack for " + kit.getName() + " putting with " + player.getName() + ": ");
            System.out.print("ConifgItemName: " + configItemName);
            System.out.print("Material: " + itemStack.getType().toString());
            System.out.print("DisplayName: " + itemStack.getItemMeta().getDisplayName());
            int slot = pathInventory.getInt("." + configItemName + ".slot");


            if (itemStack.getType().equals(Material.LEATHER_HELMET) ||
                    itemStack.getType().equals(Material.LEATHER_CHESTPLATE) ||
                    itemStack.getType().equals(Material.LEATHER_LEGGINGS) ||
                    itemStack.getType().equals(Material.LEATHER_BOOTS)) {
                LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
                meta.setColor(Teams.getArmorColorFromPlayerTeam(player));
                itemStack.setItemMeta(meta);
            }

            if (slot == 100) {
                player.getInventory().setBoots(itemStack);
            } else if (slot == 101) {
                player.getInventory().setLeggings(itemStack);
            } else if (slot == 102) {
                player.getInventory().setChestplate(itemStack);
            } else if (slot == 103) {
                player.getInventory().setHelmet(itemStack);
            } else {
                player.getInventory().setItem(slot, itemStack);
            }

        }
        kit.setPotionEffect(player);
    }

    private static ItemStack setColor(ItemStack itemStack, Player player) {
        if (itemStack != null) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            Color color;
            if (Teams.getTeamFromPlayer(player).equals(Teams.getTeamByName("red"))) {
                color = Color.fromRGB(255, 0, 0);
               // System.out.print("RED");
            } else if (Teams.getTeamFromPlayer(player).equals(Teams.getTeamByName("blue"))) {
                color = Color.fromRGB(0, 0, 255);
               // System.out.print("BLUE");
            } else {
                color = Color.WHITE;
               // System.out.print("WHITE");
            }

            leatherArmorMeta.setColor(color);
            leatherArmorMeta.setUnbreakable(true);
            itemStack.setItemMeta(leatherArmorMeta);
        }
        return itemStack;
    }

}
