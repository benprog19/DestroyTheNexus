package fuji.dtn.menus;

import fuji.dtn.util.MenuMathUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 1/1/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class MenuItem {

    ItemStack itemStack;

    Material material;
    String name;
    byte id;
    Inventory inventory;

    int slot;

    public MenuItem(Material material, String name, byte id, Inventory inventory) {
        this.material = material;
        this.name = name;
        this.id = id;
        this.inventory = inventory;

        ItemStack itemStack = new ItemStack(material, id);
        this.itemStack = itemStack;
    }

    public void setMaterial(Material material) {
        this.material = material;
        itemStack.setType(material);
    }

    public Material getMaterial() {
        return material;
    }

    public void setId(byte id) {
        this.id = id;
        itemStack.setDurability(id);
    }

    public byte getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
        itemStack.getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
    }

    public String getName() {
        return name;
    }

    public void setAmount(int amount) {
        itemStack.setAmount(amount);
    }

    public int getAmount() {
        return itemStack.getAmount();
    }

    public void setLore(List<String> lore) {
        itemStack.getItemMeta().setLore(lore);
    }

    public List<String> getLore() {
        return itemStack.getItemMeta().getLore();
    }

    public void setSlot(int x, int y) {
        slot = MenuMathUtil.calculateSlot(x, y);
    }

    public int getSlot() {
        return slot;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
