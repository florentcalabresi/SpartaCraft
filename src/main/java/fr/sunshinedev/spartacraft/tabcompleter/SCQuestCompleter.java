package fr.sunshinedev.spartacraft.tabcompleter;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SCQuestCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("scquest")) {
            if (args.length == 1) { // Premier argument
                suggestions.add("list");
                suggestions.add("random");
                suggestions.add("add");
                suggestions.add("remove");
            }else if(args.length == 3) { //DIFFICULTY QUEST
                if(args[0].equalsIgnoreCase("add")) {
                    suggestions.add("easy");
                    suggestions.add("hard");
                    suggestions.add("normal");
                }
            }else if(args.length == 4) { //TYPE QUEST
                if(args[0].equalsIgnoreCase("add")) {
                    suggestions.add("take");
                }
            }else if(args.length == 6) { //VALUE QUEST
                if(args[0].equalsIgnoreCase("add")) {
                    List<Material> m = Arrays.stream(Material.values()).toList().stream().filter(material -> material.name().startsWith(args[5])).toList();
                    for (Material material : m) {
                        ItemStack is = new ItemStack(material);
                        suggestions.add(is.getType().name());
                    }
                }
            }
        }
        return suggestions;
    }
}
