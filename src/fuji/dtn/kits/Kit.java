package fuji.dtn.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
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

    ArrayList<UUID> players = new ArrayList<>();

    public Kit(String name, int price, PotionEffect potionEffect) {
        this.name = name;
        this.price = price;
        this.potionEffect = potionEffect;
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
        this.potionEffect = newEffect;
    }

    public boolean hasPlayer(Player player) {
        if (players.contains(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "You have selected the " + ChatColor.RED + name + ChatColor.GOLD + " kit.");
    }

    public Player getPlayer(Player player) {
        if (hasPlayer(player)) {
            for (int i = 0; i < players.size(); i++) {
                Player player1 = Bukkit.getPlayer(players.get(i));
                if (player1.getUniqueId().equals(player.getUniqueId())) {
                    return player1;
                }
            }
        }
        return null;
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public void setInventory(Player player) {
        ConcurrentHashMap<Integer, ItemStack> itemStackHashMap = Kits.item;
        System.out.print("Items: " + itemStackHashMap.size());
        for (Map.Entry<Integer, ItemStack> entry : itemStackHashMap.entrySet()) {
            System.out.print("Slot Num: " + entry.getKey());
            ItemStack itemStack = entry.getValue();
            System.out.print("Slot DisplayName: " + itemStack.getItemMeta().getDisplayName());
            player.getInventory().setItem(entry.getKey(), itemStack);
            player.updateInventory();
        }
        setPotionEffect(player);
    }

    public void setPotionEffect(Player player) {
        player.getActivePotionEffects().clear();
        if (potionEffect != null) {
            player.addPotionEffect(new PotionEffect(potionEffect.getType(), 1000000, potionEffect.getAmplifier()));
        }
    }

}
