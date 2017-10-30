package fuji.dtn.kits;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 9/25/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class KitInventory {

    Kit kit;
    ItemStack mainItem;
    ItemStack secondItem;
    ItemStack thirdItem;
    ItemStack blocks;
    ItemStack blocks2;

    public KitInventory(Kit kit, ItemStack mainItem, ItemStack secondItem, ItemStack thirdItem, ItemStack blocks, ItemStack blocks2) {
        this.kit = kit;
        this.mainItem = mainItem;
        this.secondItem = secondItem;
        this.thirdItem = thirdItem;
        this.blocks = blocks;
        this.blocks2 = blocks2;
    }

    public void setInventory(Player player) {
        Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(0, mainItem);
        inventory.setItem(1, secondItem);
        inventory.setItem(2, thirdItem);
        inventory.setItem(3, blocks);
        inventory.setItem(4, blocks2);
        player.updateInventory();
    }
}
