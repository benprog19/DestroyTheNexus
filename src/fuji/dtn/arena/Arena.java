package fuji.dtn.arena;

import fuji.dtn.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/19/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class Arena {

    String name;
    int minPlayers;
    String creator;
    Location redLocation;
    Location blueLocation;
    boolean hasNexuses;
    Location redNexusCorner1;
    Location redNexusCorner2;
    Location blueNexusCorner1;
    Location blueNexusCorner2;
    Location arenaCorner1;
    Location arenaCorner2;

    boolean isReadyToPlay;

    public Arena(String name, String creator, int minPlayers, Location redLocation, Location blueLocation, boolean hasNexuses, Location redNexusCorner1, Location redNexusCorner2, Location blueNexusCorner1, Location blueNexusCorner2, Location arenaCorner1, Location arenaCorner2) {
        this.name = name;
        this.minPlayers = minPlayers;
        this.creator = creator;
        this.redLocation = redLocation;
        this.blueLocation = blueLocation;
        this.hasNexuses = hasNexuses;
        this.arenaCorner1 = arenaCorner1;
        this.arenaCorner2 = arenaCorner2;
        if (hasNexuses) {
            this.redNexusCorner1 = redNexusCorner1;
            this.redNexusCorner2 = redNexusCorner2;
            this.blueNexusCorner1 = blueNexusCorner1;
            this.blueNexusCorner2 = blueNexusCorner2;
        }

        this.isReadyToPlay = false;
        Arenas.registerArena(this);
    }


    public void setCreator(String newCreator) {
        this.creator = newCreator;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public void setMinPlayers(int newMinPlayers) {
        this.minPlayers = newMinPlayers;
    }

    public int getMinPlayers() {
        return this.minPlayers;
    }

    public void setNexuses(boolean enabled) {
        hasNexuses = enabled;
    }

    public boolean getHasNexses() {
        return hasNexuses;
    }

    public void setArenaCorner1(Location location) {
        setBlockLocation(location, "arenaCorner1");
        this.arenaCorner1 = location;
    }

    public void setArenaCorner2(Location location) {
        setBlockLocation(location, "arenaCorner2");
        this.arenaCorner2 = location;
    }

    public Location getArenaCorner1() {
        return createLocation("arenaCorner1", Main.arenaStorage.get().getConfigurationSection("Arenas." + name + "."));
    }

    public Location getArenaCorner2() {
        return createLocation("arenaCorner2", Main.arenaStorage.get().getConfigurationSection("Arenas." + name + "."));
    }


    public void setRedNexusCorner1(Location location) {
        setBlockLocation(location, "redNexusCorner1");
        this.redNexusCorner1 = location;
    }

    public void setRedNexusCorner2(Location location) {
        setBlockLocation(location, "redNexusCorner2");
        this.redNexusCorner2 = location;
    }

    public void setBlueNexusCorner1(Location location) {
        setBlockLocation(location, "blueNexusCorner1");
        this.blueNexusCorner1 = location;
    }

    public void setBlueNexusCorner2(Location location) {
        setBlockLocation(location, "blueNexusCorner2");
        this.blueNexusCorner2 = location;
    }

    public Location getRedNexusCorner1() {
        return createLocation("redNexusCorner1", Main.arenaStorage.get().getConfigurationSection("Arenas." + name + "."));
    }

    public Location getRedNexusCorner2() {
        return createLocation("redNexusCorner2", Main.arenaStorage.get().getConfigurationSection("Arenas." + name + "."));
    }

    public Location getBlueNexusCorner1() {
        return createLocation("blueNexusCorner1", Main.arenaStorage.get().getConfigurationSection("Arenas." + name + "."));
    }

    public Location getBlueNexusCorner2() {
        return createLocation("blueNexusCorner2", Main.arenaStorage.get().getConfigurationSection("Arenas." + name + "."));
    }

    public void setRedLocation(Location location) {
        setLocation(location, "redLocation");
        this.redLocation = location;
    }

    public void setBlueLocation(Location location) {
        setLocation(location, "blueLocation");
        this.blueLocation = location;
    }

    public Location getRedLocation() {
        return redLocation;
    }

    public Location getBlueLocation() {
        return blueLocation;
    }

    private void setLocation(Location location, String nodeType) {
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".x", location.getX());
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".y", location.getY());
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".z", location.getZ());
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".yaw", location.getYaw());
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".pitch", location.getPitch());
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".world", location.getWorld().getName());
        Main.arenaStorage.save();
    }

    private void setBlockLocation(Location location, String nodeType) {
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".x", location.getBlockX());
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".y", location.getBlockY());
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".z", location.getBlockZ());
        Main.arenaStorage.get().set("Arenas." + name + "." + nodeType + ".world", location.getWorld().getName());
        Main.arenaStorage.save();
    }

    public boolean isPlayable() {
        if (this.redLocation != null && this.blueLocation != null && this.redNexusCorner1 != null && this.redNexusCorner2 != null && this.blueNexusCorner1 != null && this.blueNexusCorner2 != null
                && this.arenaCorner1 != null && this.arenaCorner2 != null) {
            this.isReadyToPlay = true;
        }
        return this.isReadyToPlay;
    }

    public static Arena createArena(ConfigurationSection config) {

        Location redLocation = createLocation("redLocation", config);
        Location blueLocation = createLocation("blueLocation", config);
        Location redNexusCorner1 = createLocation("redNexusCorner1", config);
        Location redNexusCorner2 = createLocation("redNexusCorner2", config);
        Location blueNexusCorner1 = createLocation("blueNexusCorner1", config);
        Location blueNexusCorner2 = createLocation("blueNexusCorner2", config);
        Location arenaCorner1 = createLocation("arenaCorner1", config);
        Location arenaCorner2 = createLocation("arenaCorner2", config);

        Arena arena = new Arena(config.getString("name"),
                config.getString("creator"),
                config.getInt("minPlayers"),
                redLocation,
                blueLocation,
                true,
                redNexusCorner1,
                redNexusCorner2,
                blueNexusCorner1,
                blueNexusCorner2,
                arenaCorner1,
                arenaCorner2);

        return arena;
    }

    static Location createLocation(String configNode, ConfigurationSection config) {

        Location location = null;

        String world = config.getString(configNode + ".world");
        double x = config.getDouble(configNode + ".x");
        double y = config.getDouble(configNode + ".y");
        double z = config.getDouble(configNode + ".z");
        int yaw = config.getInt(configNode + ".yaw");
        int pitch = config.getInt(configNode + ".pitch");

        // Numbers assumed 0.

        if (world != null) {
            World worldObj = Bukkit.getWorld(world);
            if (worldObj != null) {
                location = new Location(worldObj, x, y, z, yaw, pitch);
            }
        }
        return location;
    }

}
