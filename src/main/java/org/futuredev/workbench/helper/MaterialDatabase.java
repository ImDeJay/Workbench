package org.futuredev.workbench.helper;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MaterialDatabase {

    HashMap<Integer, HashMap<Integer, List<String>>> blocks, items;

    public MaterialDatabase (File read) {

        YamlConfiguration file = YamlConfiguration.loadConfiguration(read);

        this.blocks = process(file.getConfigurationSection("blocks").getKeys(true), file);
        this.items  = process(file.getConfigurationSection("items").getKeys(true), file);

    }

    public ItemStack matchItem (String input, boolean blocks, boolean items) {

        if (!input.contains(":")) {
            input += ":0";
        }

        String name = input.split(":")[0], damage = input.split(":")[1];

        int id = -1, data = 0;

        if (blocks || !items) {
           for (int i : this.blocks.keySet()) {
               if (Integer.toString(i).equals(name)) {
                   id = i;
                   break;
               }

               HashMap<Integer, List<String>> map = this.blocks.get(i);
               for (String main : map.get(0)) {
                   if (name.matches("(i?)" + main)) {
                       id = i;
                       break;
                   }
               }

               if (id == -1)
                   continue;

               for (int j : map.keySet()) {
                   if (Integer.toString(j).equals(damage)) {
                       data = j;
                       break;
                   }

                   for (int k = 0; k < 2; ++k) {
                       if (name.matches("(i?)" + map.get(j).get(k))) {
                           id = i;
                           data = j;
                           break;
                       }
                   }

                   for (int k = 2; k < map.get(j).size(); ++k) {
                       if (name.matches("(i?)" + map.get(j).get(k))) {
                           data = j;
                       }
                   }

               }
           }
        }

        if ((items || !blocks) && id == -1) {
            for (int i : this.items.keySet()) {
                if (Integer.toString(i).equals(name)) {
                    id = i;
                    break;
                }

                HashMap<Integer, List<String>> map = this.items.get(i);
                for (String main : map.get(0)) {
                    if (name.matches("(i?)" + main)) {
                        id = i;
                        break;
                    }
                }

                for (int j : map.keySet()) {
                    if (id == -1) // Try special names, like 'spruce wood planks'
                        for (int k = 0; k < 2; ++k) {
                            if (name.matches("(i?)" + map.get(j).get(k))) {
                                id = i;
                                data = j;
                                break;
                            }
                        }

                    if (id == -1) // This is not the item.
                        continue;

                    if (Integer.toString(j).equals(damage)) {
                        data = j;
                        break;
                    }

                    for (int k = 2; k < map.get(j).size(); ++k) {
                        if (name.matches("(i?)" + map.get(j).get(k))) {
                            data = j;
                        }
                    }

                }
            }
        }

        return new ItemStack(id, 1, (short) data);
    }

    public String getName (ItemStack item) {
        return this.getName(item.getTypeId(), item.getDurability(), item.getAmount());
    }

    public String getName (int... info) {
        switch (info.length) {
            case 0: return null;

            case 1: // Item only
                if (this.blocks.containsKey(info[0]))
                    return this.blocks.get(info[0]).get(0).get(0);
                else return this.items.get(info[0]).get(0).get(0);

            case 2: // Item and data
                if (this.blocks.containsKey(info[0]))
                    return this.blocks.get(info[0]).get(info[1]).get(0);
                else return this.items.get(info[0]).get(info[1]).get(0);

            case 3: // Item, data, amount
            default:
                if (this.blocks.containsKey(info[0]))
                    return this.blocks.get(info[0]).get(info[1]).get(info[2] == 1 ? 0 : 1);
                else return this.items.get(info[0]).get(info[1]).get(info[2] == 1 ? 0 : 1);
        }
    }

    private HashMap<Integer, HashMap<Integer, List<String>>> process (Set<String> keys, YamlConfiguration from) {
        HashMap<Integer, HashMap<Integer, List<String>>> result = new HashMap<Integer, HashMap<Integer, List<String>>>();
        for (String key : keys) {
            int id = 0, data = 0;
            List<String> names = from.getStringList(key);
            switch (key.split(".").length) {
                case 2: // blocks.5
                    data = 0;
                    try { id = Integer.parseInt(key.split(".")[1]); }
                    catch (NumberFormatException e) { /* Do nothing! */ }
                    break;

                case 3: // blocks.5.1
                    try {
                        id = Integer.parseInt(key.split(".")[0]);
                        data = Integer.parseInt(key.split(".")[1]);
                  } catch (NumberFormatException e) {
                        // do nothing, again
                    }
            }

            if (!result.containsKey(id)) {
                HashMap<Integer, List<String>> items = new HashMap<Integer, List<String>>();
                items.put(data, names);
                result.put(id, items);
            }
        }

        return result;
    }

}