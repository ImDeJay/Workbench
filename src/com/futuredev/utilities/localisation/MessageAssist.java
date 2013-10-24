package com.futuredev.utilities.localisation;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

/**
 * Represents an instantiable message map.
 * @author afistofirony, ProjectInfinity
 */
public class MessageAssist {

    File messageFile;
    JavaPlugin plugin;
    String fileName;
    String languageName;
    FileConfiguration fileConfig;
    Map<String, String> messageMap = new HashMap<String, String>();
    Map<String, String> defaults = new HashMap<String, String>();
    Scheme colourScheme;

    /**
     * Creates a new message map.
     * @param plugin JavaPlugin to load the file from.
     * @param scheme The colour scheme this map will use.
     * @param fileName The YML file to load from, without the extension.
     */
    public MessageAssist (JavaPlugin plugin, String title, String scheme, String fileName) {

        this.plugin   = plugin;
        this.fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
        languageName  = title;
        colourScheme  = Scheme.fromString(scheme);
        messageFile   = new File(this.plugin.getDataFolder() + "languages" + File.separator, this.fileName);
        fileConfig    = new YamlConfiguration();
        defaults      = setupDefaults();
        reload();

        if (!messageFile.exists()) {
            languageName = "British English";
            messageMap = defaults;
        }

        setupMessageMap();

    }

    /**
     * Gets the colour scheme.
     * @return The colour scheme.
     */
    public Scheme getScheme ()       { return  colourScheme; }

    /**
     * Changes the colour scheme.
     * @param s The colour scheme to load.
     */
    public void setScheme (Scheme s) { colourScheme = s; }

    /**
     * Reloads the message assistant.
     */
    public void reload () {
        InputStream inputStream = plugin.getResource(fileName);
        if (inputStream != null) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(inputStream);
            fileConfig.setDefaults(config);
        }
    }

    /**
     * Sets up the message map.
     */
    private Map<String, String> setupMessageMap () {
        Map<String, String> result = new HashMap<String, String>();
        Set<String> messages = fileConfig.getKeys(true);
        for (String s : messages) {
            result.put(s, fileConfig.getString(s));
      } return result;
    }

    /**
     * Sets up the defaults in case a file does not exist.
     * @return
     */
    private Map<String, String> setupDefaults () {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(plugin.getResource("messages-en_UK.yml"));
        fileConfig.setDefaults(conf);

        Map<String, String> result = new HashMap<String, String>();
        Set<String> messages = fileConfig.getKeys(true);
        for (String s : messages) {
            result.put(s, fileConfig.getString(s));
      } return result;
    }

    /**
     * Reads a message from the message map.
     * @param str The path of the message.
     * @param variables Variables to format the message with.
     * @return The formatted message.
     */
    public String read (String str, Object ... variables) {
        Object obj = messageMap.get(str);
        if (obj == null || !(obj instanceof String)) {
            if (!fileConfig.getDefaults().contains(str)) {
                return ChatColor.RED + "Unable to find a value for " + str + ".";
          } messageMap.put(str, fileConfig.getDefaults().getString(str));
            fileConfig.set(str, fileConfig.getDefaults().getString(str));
            obj = fileConfig.getDefaults().getString(str);

        }

        String msg = (String) obj;
        List<Object> params = Arrays.asList(variables);
        List<String> finalVariables = new ArrayList<String>();
        for (Object obj2 : params) {
            if (obj2 instanceof Integer) { // Auto-convert numbers.
                String num = this.number((Integer) obj2);
                // No match? Try adding thousands commas.
                finalVariables.add(num.equals(Integer.toString((Integer) obj2)) ?
                        StringHelper.thousands((Integer) obj2,
                                this.read("Plurals.ThousandsSeparator").toCharArray()[0]) : num);
                continue;
            }

            String var = (String) obj2;
            if (var.charAt(0) == '§') {
                String[] keys = var.split("|");
                finalVariables.add(read(keys[0], StringHelper.trim(keys, 1, keys.length)));
            }
        }

        msg = MessageFormat.format(msg, finalVariables.toArray(new String[finalVariables.size()]));
        return transformMessage(msg, true, false, true);
    }

    /**
     * Used to check if a certain message is loaded.
     * @param str The path of the message.
     * @return Whether or not it is loaded.
     */
    public boolean messageExists (String str) {
        return messageMap.containsKey(str);
    }

    /**
     * Returns a nice number, if available in the message file.
     * @param value The number to try to find.
     * @return The text version of that number, or the number itself if no text was found.
     */
    public String number (int value) {
        if (messageExists("Numbers." + value))
            return read("Numbers." + value);
        return Integer.toString(value);
    }

    /**
     * Returns an ordinal for the number.
     * @param value The value.
     * @return The value with an ordinal attached to it.
     */
    public String ordinal (int value) {
        String result = Integer.toString(value);
        char num = result.charAt(result.length() - 1);
        char preNum = result.length() > 1 ? result.charAt(result.length() - 2) : '0';
        switch (num) {
            case '1': // 21st, but not 11st
                return read("Numbers.Ordinal" + (preNum == '1' ? "Rest" : "One"), value);

            case '2': // 22nd, but not 12nd
                return read("Numbers.Ordinal" + (preNum == '1' ? "Rest" : "Two"), value);

            case '3': // 23rd, but not 13rd
                return read("Numbers.Ordinal" + (preNum == '1' ? "Rest" : "Three"), value);

            default:  // 24th and 14th
                return read("Numbers.OrdinalRest", value);
        }
    }

    /**
     * Parses chat colours.
     * @param msg The message to parse colours within.
     * @return A parsed message.
     */
    public String parseChatColours (String msg) {
        for (ChatColor colour : ChatColor.values()) {
            msg = msg.replaceAll("&" + colour.getChar(), colour.toString());
      } return msg;
    }

    /**
     * Removes colour codes from the message.
     * @param msg The message to remove colours from.
     * @return The parsed message.
     */
    public String stripColour (String msg) {
        for(ChatColor colour : ChatColor.values()){
            msg = msg.replaceAll("&" + colour.getChar(), "");
      } return msg;
    }

    /**
     * Transforms a message.
     * @param msg The message to transform.
     * @param colours Whether or not to parse colours.
     * @param strip Whether or not to strip colours. This takes priority above the colours parameter.
     * @param parseKeys Whether or not to parse our colour keys.
     * @return The transformed message.
     */
    public String transformMessage (String msg, boolean colours,
                                    boolean strip, boolean parseKeys) {
        msg = (strip ? stripColour(msg) : msg);

        msg = (colours ? parseChatColours(msg) : msg);
        msg = (parseKeys ? parseVariables(msg) : msg);

        return msg.replaceAll("$n", "\n");
    }

    /**
     * Parses colour keys. This is how we achieve instant recolouring.
     * @param msg The message to parse.
     * @return THe parsed message.
     */
    public String parseVariables (String msg) {
        if (msg.contains("%p"))
            msg = msg.replaceAll("%p", colourScheme.getPrimary().toString());
        if (msg.contains("%s"))
            msg = msg.replaceAll("%s", colourScheme.getSecondary().toString());
        if (msg.contains("%t"))
            msg = msg.replaceAll("%t", colourScheme.getTertiary().toString());
        if (msg.contains("%a"))
            msg = msg.replaceAll("%a", colourScheme.getCompleted().toString());
        if (msg.contains("%n"))
            msg = msg.replaceAll("%n", colourScheme.getError().toString());
        if (msg.contains("%m"))
            msg = msg.replaceAll("%m", colourScheme.getMod().toString());
        return msg;
    }

    public String pluralise (int amount, String singular, String plural) {
        return amount == 1 ? singular : plural;
    }

    /**
     * Returns the language name.
     */
    public String toString () {
        return languageName;
    }

}