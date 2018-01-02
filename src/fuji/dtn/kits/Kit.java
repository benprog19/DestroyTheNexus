package fuji.dtn.kits;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

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



    public void setPotionEffect(Player player) {
        player.getActivePotionEffects().clear();
        if (potionEffect != null) {
            player.addPotionEffect(potionEffect);
        }
    }



}
