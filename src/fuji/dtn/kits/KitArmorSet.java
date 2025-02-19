package fuji.dtn.kits;

import fuji.dtn.main.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 11/29/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */

@Deprecated
public class KitArmorSet {

    ItemStack helmet;
    ItemStack chestplate;
    ItemStack leggings;
    ItemStack boots;

    String id;

    static ConcurrentHashMap<String, ItemStack> validArmorType = new ConcurrentHashMap<>();

    public KitArmorSet(String kitName) {
        System.out.print("Creating Armor Set for: " + kitName);
        ConfigurationSection idsConfig = Main.kitStorage.get().getConfigurationSection("Kits." + kitName + ".Inventory");
        Set<String> ids = idsConfig.getKeys(false);
        Iterator<String> iterator = ids.iterator();

        while (iterator.hasNext()) {
            String id = iterator.next();

            Set<String> idInformation = idsConfig.getConfigurationSection("." + id).getKeys(false);
            Iterator<String> iteratorInfo = idInformation.iterator();
            System.out.print("-- IDInformation From Config -- : " + idInformation.size() + " : [" + idInformation.toString() + "]");
            while (iteratorInfo.hasNext()) {
                String info = iteratorInfo.next();
                if (info.equals("armorType")) {
                    this.id = id;
                    validArmorType.put(id, Kits.createItemStack(idsConfig.getConfigurationSection("." + id)));
                }
            }
        }

        for (Map.Entry<String, ItemStack> entry : validArmorType.entrySet()) {
            if (Main.
                    kitStorage
                    .get()
                    .getString("Kits." +
                            kitName + ".Inventory." +
                            entry.getKey() +
                            ".armorType")
                    .equalsIgnoreCase("helmet")) {
                helmet = entry.getValue();

            } else if (Main.kitStorage.get().getString("Kits." + kitName + ".Inventory." + entry.getKey() + ".armorType").equalsIgnoreCase("chestplate")) {
                chestplate = entry.getValue();

            } else if (Main.kitStorage.get().getString("Kits." + kitName + ".Inventory." + entry.getKey() + ".armorType").equalsIgnoreCase("leggings")) {
                leggings = entry.getValue();

            } else if (Main.kitStorage.get().getString("Kits." + kitName + ".Inventory." + entry.getKey() + ".armorType").equalsIgnoreCase("boots")) {
                boots = entry.getValue();

            }
        }
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public ItemStack get(String type) {
        if (validArmorType.get(type) != null) {
            return validArmorType.get(type);
        } else {
            throw new NullPointerException();
        }
    }

    public void listIDs() {
        for (Map.Entry<String, ItemStack> map : validArmorType.entrySet()) {
            //System.out.print("ID: " + map.getKey() + "  IS: " + map.getValue().getType().toString());
        }
    }

}
