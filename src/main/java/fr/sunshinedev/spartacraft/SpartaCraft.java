package fr.sunshinedev.spartacraft;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.sunshinedev.spartacraft.executors.SCQuestExecutor;
import fr.sunshinedev.spartacraft.listeners.PlayerListener;
import fr.sunshinedev.spartacraft.managers.AdvancementManager;
import fr.sunshinedev.spartacraft.managers.SQLManager;
import fr.sunshinedev.spartacraft.objects.SCPlayer;
import fr.sunshinedev.spartacraft.objects.SCQuest;
import fr.sunshinedev.spartacraft.sql.models.QuestModel;
import fr.sunshinedev.spartacraft.tabcompleter.SCQuestCompleter;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class SpartaCraft extends JavaPlugin implements Listener {

    public static SpartaCraft instance;
    List<SCPlayer> players = new ArrayList<SCPlayer>();
    public static final long TIME_WINDOW = 60_000;
    UltimateAdvancementAPI instanceUAA;
    private AdvancementTab advancementTab;
    private SQLManager sqlManager;
    private ScoreboardLibrary scoreboardLibrary;
    private List<SCQuest> quests;
    private List<SCQuest> questsCurrent = new ArrayList<>();

    @Override
    public void onEnable() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        instance = this;

        sqlManager = new SQLManager();
        sqlManager.connect();

        quests = sqlManager.getTable("quests").listRecords();

        instanceUAA = UltimateAdvancementAPI.getInstance(this);
        advancementTab = instanceUAA.createAdvancementTab("spartacraft");
        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.GRASS_BLOCK, "SpartaCraft", AdvancementFrameType.TASK, true, true, 0, 0, "Tous vos succès sur SpartaCraft sont ici.");
        RootAdvancement root = new RootAdvancement(advancementTab, "root", rootDisplay, "textures/block/stone.png");
        AdvancementManager.register(advancementTab, root);


        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        if(scoreboardLibrary == null) {
            try {
                scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(getInstance());
            } catch (NoPacketAdapterAvailableException e) {
                scoreboardLibrary = new NoopScoreboardLibrary();
                getLogger().warning("No scoreboard packet adapter available!");
            }
        }

        getCommand("scquest").setExecutor(new SCQuestExecutor());
        getCommand("scquest").setTabCompleter(new SCQuestCompleter());


        players.clear();
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            players.add(new SCPlayer(player));
            advancementTab.showTab(player);
        });
    }

    @Override
    public void onDisable() {
        scoreboardLibrary.close();
        sqlManager.closeConnection();
    }

    public static SpartaCraft getInstance() {
        return instance;
    }

    public @NotNull ScoreboardLibrary getScoreboardLibrary() {
        return scoreboardLibrary;
    }

    public AdvancementTab getAdvancementTab() {
        return advancementTab;
    }

    public List<SCPlayer> getPlayers() {
        return players;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    public List<SCQuest> getQuests() {
        return quests;
    }

    public @Nullable List<SCQuest> getQuestsCurrent() {
        return questsCurrent;
    }

    public void setQuestCurrent(List<SCQuest> list) {
        questsCurrent = list;
    }
}

