package fuji.dtn.game;

import fuji.dtn.arena.ResetArena;
import fuji.dtn.kits.Kit;
import fuji.dtn.kits.Kits;
import fuji.dtn.main.Main;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.util.InstantFireworkUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

/**
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 * Created by Ben on 8/27/2017.
 * -=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 */
public class GameTimer {

    int count;
    ArrayList<UUID> players;
    BukkitTask runnable;

    public GameTimer(int count, ArrayList<UUID> players) {
        if (count >= 1) {
            this.count = count;
            this.players = players;
        }
    }

    public void startCountdown() {
        Players.fixPlayerSetup(players);

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                count--;
                if (count == 120 || count == 60 || count == 30 || count == 25 || count == 20 || count == 15 || count == 10 || (count <= 5 && count > 0) ) {
                    for (int i = 0; i < players.size(); i++) {
                        Player player = Bukkit.getPlayer(players.get(i));
                        if (player.isOnline()) {
                            player.sendMessage(ChatColor.GOLD + "Match starting in " + ChatColor.RED + count + " seconds...");
                        }
                    }
                }

                if (count == 0) {
                    ResetArena.resetArena(Rotation.getCurrentArena());
                    Players.teleportPlayerToTeams(null, true);
                    GameState.setGameState(GameState.INGAME);
                    for (int i = 0; i < players.size(); i++) {
                        Player player = Bukkit.getPlayer(players.get(i));
                        System.out.print(players.size() + " players.");
                        if (player.isOnline()) {
                            player.setHealth(20);
                            player.setFoodLevel(20);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 255));
                            player.sendMessage(ChatColor.GREEN + "Match starting... ");
                            player.setGameMode(GameMode.SURVIVAL);
                            player.getWorld().strikeLightningEffect(Rotation.getCurrentArena().getRedLocation());
                            player.getWorld().strikeLightningEffect(Rotation.getCurrentArena().getBlueLocation());
                            Kit kit = Kits.getKitByPlayer(player);

                            if (kit != null) {
                                kit.setInventory(player);
                                if (kit.getPotionEffect() != null) {
                                    player.addPotionEffect(PotionEffectType.getByName(kit.getPotionEffect().getType().toString()).createEffect(kit.getPotionEffect().getDuration(), kit.getPotionEffect().getAmplifier()));
                                }
                            } else {
                                Kit adefault = Kits.getKitByName("Standard");
                                if (adefault != null) {
                                    adefault.addPlayer(player);
                                    adefault.setInventory(player);
                                }

                            }

                        }
                    }
                    cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }



    public void endingGame(final Team winningTeam, final Team losingTeam) {
        runnable = new BukkitRunnable() {

            @Override
            public void run() {
                count--;
                if (count == 120 || count == 60 || count == 30 || count == 25 || count == 20 || count == 15 || count == 10 || (count <= 5 && count > 0) ) {
                    for (int i = 0; i < players.size(); i++) {
                        Player player = Bukkit.getPlayer(players.get(i));
                        if (player.isOnline()) {
                            player.sendMessage(ChatColor.GOLD + "Match rotating in " + ChatColor.RED + count + " seconds...");
                            final FireworkEffect red = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.RED).build();

                             final FireworkEffect blue = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.BLUE).build();

                            if (winningTeam.getColor().equals(ChatColor.RED)) {
                                new InstantFireworkUtil(red,
                                        new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 2,
                                        player.getLocation().getZ()));
                            } else if (winningTeam.getColor().equals(ChatColor.BLUE)) {
                                new InstantFireworkUtil(blue,
                                        new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 2,
                                                player.getLocation().getZ()));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    public void stop() {
        runnable.cancel();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }



}
