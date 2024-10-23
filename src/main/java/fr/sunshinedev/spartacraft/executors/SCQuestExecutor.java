package fr.sunshinedev.spartacraft.executors;

import fr.sunshinedev.spartacraft.SpartaCraft;
import fr.sunshinedev.spartacraft.Utils;
import fr.sunshinedev.spartacraft.objects.SCPlayer;
import fr.sunshinedev.spartacraft.objects.SCPlayerQuest;
import fr.sunshinedev.spartacraft.objects.SCQuest;
import fr.sunshinedev.spartacraft.sql.models.QuestModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class SCQuestExecutor implements CommandExecutor {

    private String[] difficultyFilter = new String[]{"easy", "normal", "hard"};
    private String[] typeFilter = new String[]{"take"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args[0].isEmpty()) return false;
        if (sender instanceof Player && args[0].equalsIgnoreCase("list")) {
            Player playerSender = (Player) sender;
            sender.sendMessage("===========================");
            sender.sendMessage("=     Liste des quêtes    =");
            for(SCQuest quest : SpartaCraft.getInstance().getQuests()) {
                sender.sendMessage(quest.getName() + " - " + quest.getDifficulty() + " - " + quest.getType() + " - " + quest.getQuestValue());
            }
            sender.sendMessage("===========================");
            return true;
        } else if (sender instanceof Player && args[0].equalsIgnoreCase("add")) {
            Player playerSender = (Player) sender;
            if(args.length == 6) {
                String name = args[1];
                String difficulty = args[2];
                String type = args[3];
                String amount = args[4];
                String value = args[5];
                if(name.length() <= 3) { playerSender.sendMessage("Le nom de la quête doit être supérieur à trois caractères"); return true; }
                if(Arrays.stream(difficultyFilter).noneMatch(d -> d.equalsIgnoreCase(difficulty))) {
                    playerSender.sendMessage("La difficulté n'est pas valide.");
                    return true;
                }
                if(Arrays.stream(typeFilter).noneMatch(d -> d.equalsIgnoreCase(type))) {
                    playerSender.sendMessage("Le type n'est pas valide.");
                    return true;
                }
                if(!Utils.isInteger(amount)) {
                    playerSender.sendMessage("Le montant n'est pas valide");
                    return true;
                }

                UUID uuid = UUID.randomUUID();

                HashMap<Integer, Object> valuesSQL = new HashMap<>();
                valuesSQL.put(1, uuid);
                valuesSQL.put(2, name);
                valuesSQL.put(3, difficulty);
                valuesSQL.put(4, type);
                valuesSQL.put(5, amount);
                valuesSQL.put(6, value);

                SpartaCraft.getInstance().getQuests().add(new SCQuest(uuid.toString(), name, difficulty, type, Integer.parseInt(amount), value));

                try {
                    SpartaCraft.getInstance().getSqlManager().getTable("quests").create(valuesSQL);
                    playerSender.sendMessage("La quête " + name + " a été ajoutée");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
            return false;
        } else if (sender instanceof Player && args[0].equalsIgnoreCase("remove")) {
            Player playerSender = (Player) sender;
            sender.sendMessage("REMOVE QUEST");
            return true;
        } else if (args[0].equalsIgnoreCase("random")) {
            List<SCQuest> quests = Utils.getRandomObjects(SpartaCraft.getInstance().getQuests(), 3);
            SpartaCraft.getInstance().setQuestCurrent(quests);
            for(SCPlayer player : SpartaCraft.getInstance().getPlayers()) {
                for (SCQuest quest : quests) {
                    player.playerQuests.add(new SCPlayerQuest(0, quest.getUuid()));
                }
                player.updateQuestCurrent();
            }

            return true;
        }else{
            sender.sendMessage("Command not found");
            return false;
        }
    }
}
