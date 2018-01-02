package fuji.dtn.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 1/1/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class MenuMathUtil {

    public static int calculateSlot(int x, int y) {

        // Numbers start at one.

        return (y - 1) * 9 + (x - 1);
    }

    public static ItemStack getItemAtSlot(Inventory inventory, int x, int y) {

        // Numbers start at one.

        int slot = (y - 1) * 9 + (x - 1);
        return inventory.getItem(slot);
    }
}
