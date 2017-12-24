package fuji.dtn.kits;

import fuji.dtn.main.Main;
import fuji.dtn.teams.Teams;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 9/8/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Kit {

    String name;
    int price;
    PotionEffect potionEffect;
    boolean zdefault;


    public Kit(String name, int price, PotionEffect potionEffect, boolean zdefault) {
        this.name = name;
        this.price = price;
        if (potionEffect != null) {
            this.potionEffect = new PotionEffect(potionEffect.getType(), 1000000, potionEffect.getAmplifier());
        }
        this.zdefault = zdefault;
        Kits.registerKit(this);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public PotionEffect getPotionEffect() {
        return potionEffect;
    }

    public void setPotionEffect(PotionEffect newEffect) {
        this.potionEffect = new PotionEffect(newEffect.getType(), 1000000, newEffect.getAmplifier());
    }

    public boolean isDefault() {
        return zdefault;
    }

    public void setInventory(Player player) {
        player.getInventory().clear();
        player.updateInventory();
        ConcurrentHashMap<Integer, ItemStack> itemStackHashMap = Kits.item;
        for (Map.Entry<Integer, ItemStack> entry : itemStackHashMap.entrySet()) {
            if (entry.getValue().getType().equals(Material.LEATHER_HELMET) || entry.getValue().getType().equals(Material.LEATHER_CHESTPLATE)
                    || entry.getValue().getType().equals(Material.LEATHER_LEGGINGS) || entry.getValue().getType().equals(Material.LEATHER_BOOTS)) {
                if (Main.kitStorage.get().getBoolean("Kits." + name + ".coloredArmor")) {
                    KitArmorSet kitArmorSet = new KitArmorSet(name);

                    ItemStack itemStack;
                    if (entry.getValue().getType().equals(Material.LEATHER_HELMET)) {
                        System.out.print("Helmet");
                        itemStack = kitArmorSet.getHelmet();
                        ItemStack itemStackEdit = setColor(itemStack, player);
                        player.getInventory().setHelmet(itemStackEdit);
                    } else if (entry.getValue().getType().equals(Material.LEATHER_CHESTPLATE)) {
                        System.out.print("Chest");
                        itemStack = kitArmorSet.getChestplate();
                        ItemStack itemStackEdit = setColor(itemStack, player);
                        player.getInventory().setChestplate(itemStackEdit);
                    } else if (entry.getValue().getType().equals(Material.LEATHER_LEGGINGS)) {
                        System.out.print("Legs");
                        itemStack = kitArmorSet.getLeggings();
                        ItemStack itemStackEdit = setColor(itemStack, player);
                        player.getInventory().setLeggings(itemStackEdit);
                    } else if (entry.getValue().getType().equals(Material.LEATHER_BOOTS)) {
                        System.out.print("Boots");
                        itemStack = kitArmorSet.getBoots();
                        ItemStack itemStackEdit = setColor(itemStack, player);
                        player.getInventory().setBoots(itemStackEdit);
                    }
                }


//                        leatherArmorMeta.setColor(color);
//                        entry.getValue().setItemMeta(leatherArmorMeta);
//
//                        if (entry.getValue().getType().equals(Material.LEATHER_HELMET)) {
//                            System.out.print("Helmet Detected");
//                            //player.getInventory().setHelmet(itemStack);
//                            player.getInventory().setItem(5, itemStack);
//                        } else if (entry.getValue().getType().equals(Material.LEATHER_CHESTPLATE)) {
//                            System.out.print("Chestplate Detected");
//                            //player.getInventory().setChestplate(itemStack);
//                            player.getInventory().setItem(6, itemStack);
//                        } else if (entry.getValue().getType().equals(Material.LEATHER_LEGGINGS)) {
//                            System.out.print("Leggings Detected");
//                            //player.getInventory().setLeggings(itemStack);
//                            player.getInventory().setItem(7, itemStack);
//                        } else if (entry.getValue().getType().equals(Material.LEATHER_BOOTS)) {
//                            System.out.print("Boots Detected");
//                            //player.getInventory().setBoots(itemStack);
//                            player.getInventory().setItem(8, itemStack);
//                        }



            } else {
                System.out.print("Slot Num: " + entry.getKey());
                ItemStack itemStack = entry.getValue();
                System.out.print("Slot DisplayName: " + itemStack.getItemMeta().getDisplayName());
                player.getInventory().setItem(entry.getKey(), itemStack);

                //TODO: Set armor?
            }
            player.updateInventory();
        }
        setPotionEffect(player);
    }

    public void setPotionEffect(Player player) {
        player.getActivePotionEffects().clear();
        if (potionEffect != null) {
            player.addPotionEffect(potionEffect);
        }
    }

    private ItemStack setColor(ItemStack itemStack, Player player) {
        if (itemStack != null) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            Color color;
            if (Teams.getTeamFromPlayer(player).equals(Teams.getTeamByName("red"))) {
                color = Color.fromRGB(255, 0, 0);
                System.out.print("RED");
            } else if (Teams.getTeamFromPlayer(player).equals(Teams.getTeamByName("blue"))) {
                color = Color.fromRGB(0, 0, 255);
                System.out.print("BLUE");
            } else {
                color = Color.WHITE;
                System.out.print("WHITE");
            }

            leatherArmorMeta.setColor(color);
            leatherArmorMeta.setUnbreakable(true);
            itemStack.setItemMeta(leatherArmorMeta);
        }
        return itemStack;
    }

}
