package fr.sunshinedev.spartacraft.listeners;

import fr.sunshinedev.spartacraft.SpartaCraft;
import fr.sunshinedev.spartacraft.objects.SCPlayer;
import org.bukkit.*;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.Random;

import static fr.sunshinedev.spartacraft.SpartaCraft.TIME_WINDOW;

public class PlayerListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SpartaCraft.getInstance().getPlayersPoop().add(new SCPlayer(event.getPlayer().getUniqueId()));
        SpartaCraft.getInstance().getAdvancementTab().showTab(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SpartaCraft.getInstance().getPlayersPoop().removeIf((playerPooper -> playerPooper.uuid == event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Optional<SCPlayer> optionalPlayerPooper = SpartaCraft.getInstance().getPlayersPoop().stream().filter((playerPooper -> playerPooper.uuid == player.getUniqueId())).findFirst();
        if(!optionalPlayerPooper.isPresent()) return;
        SCPlayer playerPooper = optionalPlayerPooper.get();
        if(player.getGameMode() == GameMode.CREATIVE) return;
        if(event.isSneaking()) {
            playerPooper.handlerPoop = new BukkitRunnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    boolean poopGold = random.nextInt(100) < 3;

                    long currentTime = System.currentTimeMillis();
                    playerPooper.listPoopTimestamps.removeIf(timestamp -> currentTime - timestamp > TIME_WINDOW);
                    playerPooper.listPoopTimestamps.add(currentTime);

                    SpartaCraft.getInstance().getAdvFirstPoop().grant(player);
                    if(poopGold) {
                        SpartaCraft.getInstance().getAdvFirstPoopGold().grant(player);
                    }

                    World world = player.getWorld();
                    BlockDisplay blockDisplay = (BlockDisplay) world.spawn(player.getLocation().add(0, 0, 0), BlockDisplay.class);
                    blockDisplay.setCustomName("Caca "+ (poopGold ? "dorée" : "") +" de %s".formatted(player.getName()));
                    blockDisplay.setCustomNameVisible(true);
                    blockDisplay.setMetadata("poop", new FixedMetadataValue(SpartaCraft.getInstance(), true));
                    blockDisplay.setMetadata("poop", new FixedMetadataValue(SpartaCraft.getInstance(), player.getName()));
                    blockDisplay.setBlock(poopGold ? Material.GOLD_BLOCK.createBlockData() : Material.BROWN_TERRACOTTA.createBlockData());
                    Transformation transformation = new Transformation(
                            new Vector3f(0, 0, 0), // position
                            new AxisAngle4f(0, 0, 0, 0),
                            new Vector3f(0.2F, 0.2F, 0.2F),
                            new AxisAngle4f(0, 0, 0, 0)
                    );
                    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
                    blockDisplay.setGravity(true);
                    blockDisplay.setInvulnerable(true);
                    blockDisplay.setGlowing(true);
                    blockDisplay.setFallDistance(1);
                    blockDisplay.setGlowColorOverride(Color.BLACK);
                    blockDisplay.setTransformation(transformation);

                    if (playerPooper.listPoopTimestamps.size() >= 5) {
                        SpartaCraft.getInstance().getAdvFirstPoopDiarrhea().grant(player);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 90 * 20, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 90 * 20, 150));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 90 * 20, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 90 * 20, 1));
                        playerPooper.listPoopTimestamps.clear();
                    }

                    if(poopGold) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300 * 20, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 3));
                    }

                    player.setFoodLevel(player.getFoodLevel() - 3);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));

                    player.sendMessage("Bravo pour ce beau caca %s".formatted(player.getName()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!blockDisplay.isDead()) {
                                blockDisplay.remove();
                            }
                        }
                    }.runTaskLater(SpartaCraft.getInstance(), 200);
                }
            }.runTaskLater(SpartaCraft.getInstance(), 100);
        }else{
            if(playerPooper.handlerPoop != null) {
                playerPooper.handlerPoop.cancel();
            }
        }
    }

}
