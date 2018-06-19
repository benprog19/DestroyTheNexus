package fuji.dtn.menus;

import fuji.dtn.util.MenuMathUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
       // System.out.print("IS > Material:" + itemStack.getType().toString() + " > Amount:" + itemStack.getAmount() + " > ID:" + itemStack.getDurability());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(item.getName());
       // System.out.print("IS > Name(display):" + itemMeta.getDisplayName());
        if (item.getLore() != null) {
            List<String> lore = item.getLore();
            itemMeta.setLore(lore);
            //System.out.print("IS > Lore:" + lore.toString());
        }
        itemStack.setItemMeta(itemMeta);
        //items.add(item);
        inventory.setItem(item.getSlot(), itemStack);

        //Bukkit.getPlayer("benprog19").getInventory().setItem(item.getSlot(), itemStack);
    }

    @Deprecated
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
