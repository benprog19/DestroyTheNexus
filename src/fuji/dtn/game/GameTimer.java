package fuji.dtn.game;

import fuji.dtn.arena.ResetArena;
import fuji.dtn.events.NexusBlockBreakEvent;
import fuji.dtn.kits.Kit;
import fuji.dtn.kits.Kits;
import fuji.dtn.main.Main;
import fuji.dtn.rotation.Rotation;
import fuji.dtn.teams.Team;
import fuji.dtn.teams.Teams;
import fuji.dtn.voting.Votes;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;
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

    static boolean starting = false;

    public GameTimer(int count, ArrayList<UUID> players) {
        if (count >= 1) {
            this.count = count;
            this.players = players;
        }
    }

    public void votingCountdown() {
        starting = true;
        System.out.print("Voting started.");
        if (Rotation.getCurrentArena() == null) {
            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    count--;

                    for (Player pls : Bukkit.getOnlinePlayers()) {
                        pls.setLevel(count);
                        pls.setExp(0);
                    }

                    if ((Bukkit.getOnlinePlayers().size() < 2)) {
                        GameState.setGameState(GameState.WAITING);
                        cancel();
                        for (Player pls : Bukkit.getOnlinePlayers()) {
                            //System.out.print(Lobby.getLobbyLoc().toString());
                            pls.teleport(Lobby.getLobbyLoc());
                            pls.sendMessage(ChatColor.RED + "A player has left and there is no longer enough players to start playing the game. Please wait for more players.");
                        }
                    }

                    if (count == 120 || count == 90 || count == 60 || count == 30 || count == 15 || count == 10) {
                        for (int i = 0; i < players.size(); i++) {
                            Player player = Bukkit.getPlayer(players.get(i));
                            player.sendMessage(ChatColor.GOLD + "Type " + ChatColor.RED + "/vote" + ChatColor.GOLD
                                    + " to start voting for the next map.  The game will start in " + ChatColor.RED + count + ChatColor.GOLD + ".");
                        }
                    } else if ((count <= 5 && count > 0)) {
                        for (int i = 0; i < players.size(); i++) {
                            Player player = Bukkit.getPlayer(players.get(i));
                            player.sendMessage(ChatColor.GOLD + "The game will start in " + ChatColor.RED + count + ChatColor.GOLD + ".");
                        }
                    }

                    if (count <= 0) {
                        cancel();
                        Votes.endVote();
                        startCountdown();
                        for (Player pls : Bukkit.getOnlinePlayers()) {
                            pls.closeInventory();
                        }
                    }
                }
            }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + "An arena has already been selected.  Loading " + ChatColor.RED + Rotation.getCurrentArena().getName() + ChatColor.GOLD + "...");
            startCountdown();
        }
    }

    public void startCountdown() {
        System.out.print("Initiating countdown.");
        GameState.setGameState(GameState.STARTING);
        count = 11;
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                count--;

                for (Player pls : Bukkit.getOnlinePlayers()) {
                    pls.setLevel(count);
                    pls.setExp(0);
                }

                if (Rotation.getCurrentArena() != null) {
                    if ((Bukkit.getOnlinePlayers().size() < Rotation.getCurrentArena().getMinPlayers())) {
                        GameState.setGameState(GameState.WAITING);
                        cancel();
                        for (Player pls : Bukkit.getOnlinePlayers()) {
                            //System.out.print(Lobby.getLobbyLoc().toString());
                            pls.teleport(Lobby.getLobbyLoc());
                            pls.sendMessage(ChatColor.RED + "A player has left and there is no longer enough players to start playing the game. Please wait for more players.");
                        }
                    }


                    if (count == 120 || count == 90 || count == 60 || count == 30 || count == 25 || count == 20 || count == 15 || count == 10 || (count <= 5 && count > 0)) {
                        for (int i = 0; i < players.size(); i++) {
                            Player player = Bukkit.getPlayer(players.get(i));
                            if (player.isOnline()) {
                                //TitleAPI.sendTitle(player, 5, 40, 5, ChatColor.GOLD + "" + count, "");
                                player.sendMessage(ChatColor.GOLD + "Match starting in " + ChatColor.RED + count + " second(s)...");
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                            }
                        }
                    }

                    if (count == 0) {
                        Players.fixPlayerSetup(players);
                        ResetArena.resetArena(Rotation.getCurrentArena(), true);
                        Players.teleportPlayerToTeams(null, true);
                        GameState.setGameState(GameState.INGAME);
                        for (int i = 0; i < 5; i++) {
                            Bukkit.broadcastMessage("  ");
                        }
                        Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "                                                                                ");
                        Bukkit.broadcastMessage("  ");
                        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "    " + Rotation.getCurrentArena().getName());
                        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "       built by " + ChatColor.RED + Rotation.getCurrentArena().getCreator());
                        Bukkit.broadcastMessage("  ");
                        Bukkit.broadcastMessage("     You are " + ChatColor.RED + "" + ChatColor.BOLD +  Math.round(Rotation.getCurrentArena().getRedLocation().distance(Rotation.getCurrentArena().getBlueLocation())) + "m " + ChatColor.WHITE + "away from your opponents.");
                        Bukkit.broadcastMessage("  ");
                        Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "                                                                                ");
                        for (int i = 0; i < 3; i++) {
                            Bukkit.broadcastMessage("  ");
                        }
                        for (int i = 0; i < players.size(); i++) {
                            Player player = Bukkit.getPlayer(players.get(i));
                            System.out.print(players.size() + " > players");
                            if (player.isOnline()) {
                                for (Player pls : Bukkit.getOnlinePlayers()) {
                                    pls.showPlayer(player);
                                }
                                player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 0);
                                player.setHealth(20);
                                player.setFoodLevel(20);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 255));
                                player.getActivePotionEffects().clear();
                                player.sendMessage(ChatColor.GREEN + "Match starting... ");
                                player.setGameMode(GameMode.SURVIVAL);
                                player.getWorld().strikeLightningEffect(Rotation.getCurrentArena().getRedLocation());
                                player.getWorld().strikeLightningEffect(Rotation.getCurrentArena().getBlueLocation());

                                Kit kit = Kits.getKitByPlayer(player);
                                if (kit == null) {
                                    kit = Kits.getDefaultKit();
                                    //System.out.print("Default Kit: " + kit.getName());
                                    Kits.addPlayerToKit(player, kit);
                                }
                                //System.out.print("Setting Kit: " + kit.getName());
                                Kits.setInventory(player, kit);
                                //System.out.print("Has Kit: " + Kits.getKitByPlayer(player).getName());

                                System.out.print(" -- Teams Setup:");
                                System.out.print("    RED (" + Teams.getTeamByName("red").getPlayers().size() + ")" + Teams.getTeamByName("red").getPlayers().toString());
                                System.out.print("    BLUE (" + Teams.getTeamByName("blue").getPlayers().size() + ")" + Teams.getTeamByName("blue").getPlayers().toString());

                            }
                        }
                        cancel();
                    }
                } else {
                    cancel();
                    Bukkit.broadcastMessage(ChatColor.RED + "There was an error while loading an arena. There is a possibility there are no arenas to play.");
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }



    public void endingGame(final Team winningTeam, final Team losingTeam) {
        count = 16;
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                count--;

                for (Player pls : Bukkit.getOnlinePlayers()) {
                    pls.setLevel(count);
                    pls.setExp(0);
                }

                endingEffects(winningTeam);
                if (count == 120 || count == 60 || count == 30 || count == 25 || count == 20 || count == 15 || count == 10 || (count <= 5 && count > 0)) {
                    for (int i = 0; i < players.size(); i++) {
                        Player player = Bukkit.getPlayer(players.get(i));
                        if (player.isOnline()) {
                            player.sendMessage(ChatColor.GOLD + "Match rotating in " + ChatColor.RED + count + " seconds...");

                        }
                    }
                } else if (count == 0) {
                    cancel();
                    ResetArena.resetArena(Rotation.getCurrentArena(), true);
                    Players.resetPlayers(true);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        GameState.setGameState(GameState.WAITING);
                        player.sendMessage(ChatColor.GREEN + "Teleporting to lobby...");
                        if (Lobby.getLobbyLoc().getWorld().getName() != null) {
                            player.teleport(Lobby.getLobbyLoc());
                        } else {
                            player.sendMessage(ChatColor.RED + "Lobby location is invalid. Teleporting to your last known location.");
                        }
                        player.setHealth(20);
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                        player.setGameMode(GameMode.ADVENTURE);
                        NexusBlockBreakEvent.blocksBlue = 0;
                        NexusBlockBreakEvent.blocksRed = 0;
                        starting = false;
                        for (Player pls : Bukkit.getOnlinePlayers()) {
                            pls.showPlayer(player);
                        }
                    }
                    Game.tryStart();
                    Rotation.nullifyArena();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    private void endingEffects(Team winningTeam) {
        if (winningTeam.equals(Teams.getTeamByName("red"))) {
            Location blueLoc = Rotation.getCurrentArena().getBlueLocation();
            Location redLoc = Rotation.getCurrentArena().getRedLocation();
            Firework firework = (Firework) redLoc.getWorld().spawnEntity(redLoc, EntityType.FIREWORK);
            FireworkMeta meta = firework.getFireworkMeta();
            FireworkEffect redFw = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.RED).build();
            meta.addEffect(redFw);
            meta.setPower(2);
            firework.setFireworkMeta(meta);
            blueLoc.getWorld().strikeLightning(blueLoc);
            for (int i = 0; i < 5; i++) {
                TNTPrimed tnt = redLoc.getWorld().spawn(blueLoc, TNTPrimed.class);

                Random r = new Random();
                int Low = -2;
                int High = 2;
                int v = r.nextInt(High-Low) + Low;

                Random r1 = new Random();
                int Low1 = -2;
                int High1 = 2;
                int v1 = r1.nextInt(High1-Low1) + Low1;

                Random r2 = new Random();
                int Low2 = -2;
                int High2 = 2;
                int v2 = r2.nextInt(High2-Low2) + Low2;

                tnt.setVelocity(new Vector(v, v1, v2));
                tnt.setFuseTicks(5);
                tnt.setGravity(false);
            }
        } else if (winningTeam.equals(Teams.getTeamByName("blue"))) {
            Location redLoc = Rotation.getCurrentArena().getRedLocation();
            Location blueLoc = Rotation.getCurrentArena().getBlueLocation();

            Firework firework = (Firework) blueLoc.getWorld().spawnEntity(blueLoc, EntityType.FIREWORK);
            FireworkMeta meta = firework.getFireworkMeta();
            FireworkEffect redFw = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.BLUE).build();
            meta.addEffect(redFw);
            meta.setPower(2);
            firework.setFireworkMeta(meta);
            redLoc.getWorld().strikeLightning(redLoc);
            for (int i = 0; i < 5; i++) {
                TNTPrimed tnt = redLoc.getWorld().spawn(redLoc, TNTPrimed.class);

                Random r = new Random();
                int Low = -2;
                int High = 2;
                int v = r.nextInt(High-Low) + Low;

                Random r1 = new Random();
                int Low1 = -2;
                int High1 = 2;
                int v1 = r1.nextInt(High1-Low1) + Low1;

                Random r2 = new Random();
                int Low2 = -2;
                int High2 = 2;
                int v2 = r2.nextInt(High2-Low2) + Low2;

                tnt.setVelocity(new Vector(v, v1, v2));
                tnt.setFuseTicks(5);
                tnt.setGravity(false);
            }
        }
    }

    public void stop() {
        runnable.cancel();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }



}
