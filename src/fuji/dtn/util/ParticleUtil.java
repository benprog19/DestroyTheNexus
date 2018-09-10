package fuji.dtn.util;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 6/12/2018.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class ParticleUtil {

    public static void createHelix(Location loc, EnumParticle particle, int radius, int height) {
        for(double y = 0; y <= height; y+=0.05) {
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);
            double x1 = radius * Math.cos(y+(3.14159));
            double z1 = radius * Math.sin(y+(3.14159));
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle,true, (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
            PacketPlayOutWorldParticles packet1 = new PacketPlayOutWorldParticles(particle,true, (float) (loc.getX() + x1), (float) (loc.getY() + y), (float) (loc.getZ() + z1), 0, 0, 0, 0, 1);
            for(Player online : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
                ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet1);
            }
        }
    }

    public static void particle(Location loc,  EnumParticle particle) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle,true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 1);
        for (Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
