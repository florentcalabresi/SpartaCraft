package fr.sunshinedev.spartacraft;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.*;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public final class SpartaCraft extends JavaPlugin implements Listener {

    public SpartaCraft instance;
    List<PlayerPooper> playersPoop = new ArrayList<PlayerPooper>();
    private static final long TIME_WINDOW = 60_000;
    UltimateAdvancementAPI instanceUAA;
    private AdvancementTab advancementTab;
    private BaseAdvancement advFirstPoop;
    private BaseAdvancement advFirstPoopGold;
    private BaseAdvancement advFirstPoopDiarrhea;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        instance = this;
        instanceUAA = UltimateAdvancementAPI.getInstance(this);
        advancementTab = instanceUAA.createAdvancementTab("spartacraft");
        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.GRASS_BLOCK, "SpartaCraft", AdvancementFrameType.TASK, true, true, 0, 0, "Tous vos succès sur SpartaCraft sont ici.");
        RootAdvancement root = new RootAdvancement(advancementTab, "root", rootDisplay, "textures/block/stone.png");
        advFirstPoop = new BaseAdvancement("sc-poop-first", new AdvancementDisplay(Material.BROWN_TERRACOTTA, "Faire popo", AdvancementFrameType.TASK, true, true, 2, 0, "Effectuer votre premier caca !"), root);
        advFirstPoopGold = new BaseAdvancement("sc-poop-first-gold", new AdvancementDisplay(Material.GOLD_BLOCK, "Faire popo en mode classe", AdvancementFrameType.TASK, true, true, 2, 1, "Effectuer votre premier caca dorée !"), root);
        advFirstPoopDiarrhea = new BaseAdvancement("sc-poop-first-diarrhea", new AdvancementDisplay(Material.ROTTEN_FLESH, "Avoir une bonne diarée", AdvancementFrameType.TASK, true, true, 2, 2, "Faire caca plus de 5x en moins d'une minute"), root);
        advancementTab.registerAdvancements(root, advFirstPoop, advFirstPoopGold, advFirstPoopDiarrhea);

        playersPoop.clear();
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            playersPoop.add(new PlayerPooper(player.getUniqueId()));
            advancementTab.showTab(player);
        });
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playersPoop.add(new PlayerPooper(event.getPlayer().getUniqueId()));
        advancementTab.showTab(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playersPoop.removeIf((playerPooper -> playerPooper.uuid == event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Optional<PlayerPooper> optionalPlayerPooper = playersPoop.stream().filter((playerPooper -> playerPooper.uuid == player.getUniqueId())).findFirst();
        if(!optionalPlayerPooper.isPresent()) return;
        PlayerPooper playerPooper = optionalPlayerPooper.get();
        if(player.getGameMode() == GameMode.CREATIVE) return;
        if(event.isSneaking()) {
            playerPooper.handler = new BukkitRunnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    boolean poopGold = random.nextInt(100) < 3;

                    long currentTime = System.currentTimeMillis();
                    playerPooper.listTimestamps.removeIf(timestamp -> currentTime - timestamp > TIME_WINDOW);
                    playerPooper.listTimestamps.add(currentTime);

                    advFirstPoop.grant(player);
                    if(poopGold) {
                        advFirstPoopGold.grant(player);
                    }

                    World world = player.getWorld();
                    BlockDisplay blockDisplay = (BlockDisplay) world.spawn(player.getLocation().add(0, 0, 0), BlockDisplay.class);
                    blockDisplay.setCustomName("Caca "+ (poopGold ? "dorée" : "") +" de %s".formatted(player.getName()));
                    blockDisplay.setCustomNameVisible(true);
                    blockDisplay.setMetadata("poop", new FixedMetadataValue(instance, true));
                    blockDisplay.setMetadata("poop", new FixedMetadataValue(instance, player.getName()));
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

                    if (playerPooper.listTimestamps.size() >= 5) {
                        advFirstPoopDiarrhea.grant(player);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 90 * 20, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 90 * 20, 150));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 90 * 20, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 90 * 20, 1));
                        playerPooper.listTimestamps.clear();
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
                    }.runTaskLater(instance, 200);
                }
            }.runTaskLater(this, 100);
        }else{
            if(playerPooper.handler != null) {
                playerPooper.handler.cancel();
            }
        }
    }
}

