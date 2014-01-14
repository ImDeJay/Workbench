package org.futuredev.workbench.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.futuredev.workbench.math.Levenshtein;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EntityDatabase {

    HashMap<Integer, List<String>> entities;
    HashMap<String, List<Integer>> categories;

    public EntityDatabase (File path) {
        this(YamlConfiguration.loadConfiguration(path));
    }

    public EntityDatabase (YamlConfiguration file) {
        this.entities = new HashMap<Integer, List<String>>();

        for (String key : file.getConfigurationSection("entities").getKeys(false)) {
            String[] split = key.split("\\.");
            String data = split[split.length - 1];
            String category = "passive";
            int id;
            if (data.contains("-")) {
                category = data.split("-")[1];
                id = Integer.parseInt(data.split("-")[0]);
            }

            else id = Integer.parseInt(data);

            entities.put(id, file.getStringList(key));
            if (!categories.containsKey(category.toLowerCase()))
                categories.put(category.toLowerCase(), Arrays.asList(id));
            else categories.get(category.toLowerCase()).add(id);
        }
    }

    public EntityData lookup (String input) {
        int id = -1;
        for (int i : this.entities.keySet()) {
            if (Integer.toString(i).equals(input)) {
                id = i;
                break;
            }

            for (String name : this.entities.get(i)) {
                if (Integer.toString(i).equals(input) || input.matches("(i?)" + name)) {
                    id = i;
                    break;
                }
            }
        }

        if (id == -1)
            return null;

        return new EntityData(id);
    }

    public ArrayList<EntityData> lookupCategory (String input) {
        ArrayList<EntityData> result = new ArrayList<EntityData>();

        if (!categories.containsKey(input.toLowerCase()))
            input = Levenshtein.closest(input, categories.keySet());

        for (int id : categories.get(input))
            result.add(new EntityData(id));

        return result;
    }

    public String getDefaultName (int id, int amount) {
        List<String> items = this.entities.get(id);
        return amount == 1 ? items.get(0) : items.get(1);
    }

}