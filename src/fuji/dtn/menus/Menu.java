package fuji.dtn.menus;

import fuji.dtn.util.MenuMathUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 1/1/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Menu {

    ArrayList<MenuItem> items = new ArrayList<>();
    int slots;
    String title;

    Inventory inventory;

    public Menu(String title, int slots) {
        this.items = items;
        this.slots = slots;
        this.title = title;


        inventory = Bukkit.getServer().createInventory(null, slots, title);
    }

    public void clear() {
        inventory.clear();
    }

    public ItemStack getItemFromSlot(int x, int y) {
        return MenuMathUtil.getItemAtSlot(inventory, x, y);
    }

    public String getTitle() {
        return title;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addItem(MenuItem item) {
        ItemStack itemStack = new ItemStack(item.getMaterial(), item.getAmount(), item.getId());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(item.getName());
        itemMeta.setLore(item.getLore());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(item.getSlot(), itemStack);
    }

    public void addItems(ArrayList<MenuItem> items) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = new ItemStack(items.get(i).getMaterial(), items.get(i).getAmount(), items.get(i).getId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(items.get(i).getName());
            itemMeta.setLore(items.get(i).getLore());
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(items.get(i).getSlot(), itemStack);
        }
    }
}
