package fr.sunshinedev.spartacraft;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.sunshinedev.spartacraft.listeners.PlayerListener;
import fr.sunshinedev.spartacraft.objects.SCPlayer;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SpartaCraft extends JavaPlugin implements Listener {

    public static SpartaCraft instance;
    List<SCPlayer> players = new ArrayList<SCPlayer>();
    public static final long TIME_WINDOW = 60_000;
    UltimateAdvancementAPI instanceUAA;
    private AdvancementTab advancementTab;
    private BaseAdvancement advFirstPoop;
    private BaseAdvancement advFirstPoopGold;
    private BaseAdvancement advFirstPoopDiarrhea;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        instance = this;
        instanceUAA = UltimateAdvancementAPI.getInstance(this);
        advancementTab = instanceUAA.createAdvancementTab("spartacraft");
        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.GRASS_BLOCK, "SpartaCraft", AdvancementFrameType.TASK, true, true, 0, 0, "Tous vos succès sur SpartaCraft sont ici.");
        RootAdvancement root = new RootAdvancement(advancementTab, "root", rootDisplay, "textures/block/stone.png");
        advFirstPoop = new BaseAdvancement("sc-poop-first", new AdvancementDisplay(Material.BROWN_TERRACOTTA, "Faire popo", AdvancementFrameType.TASK, true, true, 2, 0, "Effectuer votre premier caca !"), root);
        advFirstPoopGold = new BaseAdvancement("sc-poop-first-gold", new AdvancementDisplay(Material.GOLD_BLOCK, "Faire popo en mode classe", AdvancementFrameType.TASK, true, true, 2, 1, "Effectuer votre premier caca dorée !"), root);
        advFirstPoopDiarrhea = new BaseAdvancement("sc-poop-first-diarrhea", new AdvancementDisplay(Material.ROTTEN_FLESH, "Avoir une bonne diarée", AdvancementFrameType.TASK, true, true, 2, 2, "Faire caca plus de 5x en moins d'une minute"), root);
        advancementTab.registerAdvancements(root, advFirstPoop, advFirstPoopGold, advFirstPoopDiarrhea);

        players.clear();
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            players.add(new SCPlayer(player.getUniqueId()));
            advancementTab.showTab(player);
        });
    }

    @Override
    public void onDisable() {
    }

    public static SpartaCraft getInstance() {
        return instance;
    }

    public AdvancementTab getAdvancementTab() {
        return advancementTab;
    }

    public BaseAdvancement getAdvFirstPoop() {
        return advFirstPoop;
    }

    public BaseAdvancement getAdvFirstPoopGold() {
        return advFirstPoopGold;
    }

    public BaseAdvancement getAdvFirstPoopDiarrhea() {
        return advFirstPoopDiarrhea;
    }

    public List<SCPlayer> getPlayersPoop() {
        return players;
    }
}

