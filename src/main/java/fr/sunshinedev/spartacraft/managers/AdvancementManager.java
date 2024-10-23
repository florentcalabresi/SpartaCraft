package fr.sunshinedev.spartacraft.managers;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class AdvancementManager {

    public static List<BaseAdvancement> advancementList = new ArrayList<>();
    public static BaseAdvancement advFirstPoop;
    public static BaseAdvancement advFirstPoopGold;
    public static BaseAdvancement advFirstPoopDiarrhea;

    public static void register(AdvancementTab advancementTab, RootAdvancement root) {

        advFirstPoop = new BaseAdvancement("sc-poop-first", new AdvancementDisplay(Material.BROWN_TERRACOTTA, "Faire popo", AdvancementFrameType.TASK, true, true, 2, 0, "Effectuer votre premier caca !"), root);
        advFirstPoopGold = new BaseAdvancement("sc-poop-first-gold", new AdvancementDisplay(Material.GOLD_BLOCK, "Faire popo en mode classe", AdvancementFrameType.TASK, true, true, 2, 1, "Effectuer votre premier caca dorée !"), root);
        advFirstPoopDiarrhea = new BaseAdvancement("sc-poop-first-diarrhea", new AdvancementDisplay(Material.ROTTEN_FLESH, "Avoir une bonne diarée", AdvancementFrameType.TASK, true, true, 2, 2, "Faire caca plus de 5x en moins d'une minute"), root);

        advancementTab.registerAdvancements(root,
                advFirstPoop,
                advFirstPoopDiarrhea,
                advFirstPoopGold);
    }

    public static BaseAdvancement getAdvFirstPoop() {
        return advFirstPoop;
    }

    public static BaseAdvancement getAdvFirstPoopGold() {
        return advFirstPoopGold;
    }

    public static BaseAdvancement getAdvFirstPoopDiarrhea() {
        return advFirstPoopDiarrhea;
    }

}
