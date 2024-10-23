package fr.sunshinedev.spartacraft.objects;

import fr.sunshinedev.spartacraft.SpartaCraft;
import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SCPlayer {

    private final @NotNull Sidebar sidebar;
    public UUID uuid;
    public List<Long> listPoopTimestamps = new ArrayList<>();
    public BukkitTask handlerPoop;
    public List<SCPlayerQuest> playerQuests;

    public SCPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.playerQuests = new ArrayList<>();

        List<SCQuest> scQuest = SpartaCraft.getInstance().getQuestsCurrent();
        sidebar = SpartaCraft.getInstance().getScoreboardLibrary().createSidebar();
        sidebar.title(Component.text(ChatColor.GOLD + "Sparta" + ChatColor.YELLOW + "Craft"));
        sidebar.line(0, Component.empty());
        sidebar.line(1, Component.text("Quête quotidienne:"));
        if(scQuest.isEmpty()) {
            sidebar.line(2, Component.text("N/A"));
            sidebar.line(3, Component.empty());
        }else{
            int i = 2;
            for(SCQuest quest : scQuest) {
                Optional<SCPlayerQuest> scPlayerQuest = playerQuests.stream().filter(spq -> spq.getQid().equalsIgnoreCase(quest.getUuid())).findFirst();
                if(scPlayerQuest.isEmpty()) return;
                sidebar.line(i, Component.text(quest.getName() + " - Récupérer " + quest.getQuestAmount() + " " + quest.getQuestValue() + " - " + scPlayerQuest.get().getAmount() + "/" + quest.getQuestAmount()));
                i++;
            }
            sidebar.line(i, Component.empty());
        }

        sidebar.addPlayer(player);
    }

    public void updateQuestCurrent() {
        List<SCQuest> scQuest = SpartaCraft.getInstance().getQuestsCurrent();
        if(scQuest == null) return;
        if(scQuest.isEmpty()) {
            sidebar.line(2, Component.text("N/A"));
            sidebar.line(3, Component.empty());
        }else{
            int i = 2;
            for(SCQuest quest : scQuest) {
                Optional<SCPlayerQuest> scPlayerQuest = playerQuests.stream().filter(spq -> spq.getQid().equalsIgnoreCase(quest.getUuid())).findFirst();
                if(scPlayerQuest.isEmpty()) return;
                boolean isDone =  scPlayerQuest.get().getAmount() >= quest.getQuestAmount();
                sidebar.line(i, Component.text(quest.getName() + " - Récupérer " + quest.getQuestAmount() + " " + quest.getQuestValue() + " - " + scPlayerQuest.get().getAmount() + "/" + quest.getQuestAmount() + (isDone ? ChatColor.GREEN + "✔" : ChatColor.RED + "❌")));
                i++;
            }
            sidebar.line(i, Component.empty());
        }
    }

}
