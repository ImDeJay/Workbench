package org.futuredev.workbench.localisation.json;

import org.bukkit.ChatColor;

import java.util.ArrayList;

/**
 * Represents one of the elements in a compound JSON message.
 *
 * BASE JSON ELEMENT (class based from this)
 * {
 *    text: "Text",
 *    color: "Value"
 *    clickEvent: {
 *        action: suggest_command,
 *        value: "You should say this."
 *    },
 *
 *    hoverEvent: {
 *        action: show_text,
 *        value: "Click me to insert text into the chat."
 *    }
 *
 * }
 *
 *
 * @author afistofirony
 */
public class Element {

    Action click;
    String clickValue, hoverValue;
    String text;
    ColorPattern colour;

    boolean bold, strike, underline, italic, magic;

    public Element setClickAction (Action click, String value) {
        this.click = click;
        this.clickValue = value;
        return this;
    }

    public Element setHoverValue (String value) {
        this.hoverValue = value;
        return this;
    }

    public Element setValue (String text) {
        this.text = text;
        return this;
    }

    public Element setColour (ChatColor... colours) {
        return this.setColor(colours);
    }

    public Element setColor (ChatColor... colours) {
         this.colour = new ColorPattern(colours);
         return this;
    }

    public Element setBold (boolean bold) {
        this.bold = bold;
        return this;
    }

    public Element setStrike (boolean strike) {
        this.strike = strike;
        return this;
    }

    public Element setUnderline (boolean underline) {
        this.underline = underline;
        return this;
    }

    public Element setItalic (boolean italic) {
        this.italic = italic;
        return this;
    }

    public Element setMagic (boolean magic) {
        this.magic = magic;
        return this;
    }

    public String toString (boolean brackets) {
        String result = brackets ? "{" : "";
        ArrayList<String> elements = new ArrayList<String>();
        if (text != null && !text.isEmpty()) {
            if (colour != null) {
                if (colour.pattern.length < 2) {
                    elements.add("text: '" + text.replaceAll("'", "\\\\'") + "'");
                    elements.add("color: " + colour.pattern[0].name().toLowerCase());
              } else {
                    elements.add("text: '" + colour.patterned(text.replaceAll("'", "\\\\'")) + "'");
                }
          } else {
                elements.add("text: '" + text.replaceAll("'", "\\\\'") + "'");
            }

            if (bold)
                elements.add("bold: true");

            if (italic)
                elements.add("italic: true");

            if (underline)
                elements.add("underlined: true");

            if (strike)
                elements.add("strikethrough: true");

            if (magic)
                elements.add("obfuscated: true");
        }

        if (click != null)
            elements.add("clickEvent:{ action:" + click + "," +
                    " value:'" + clickValue.replaceAll("'", "\\\\'") + "'}");

        if (hoverValue != null && !hoverValue.isEmpty()) {
            String element = "hoverEvent:{action:" + (hoverValue.contains("\n") ? "show_item" : "show_text")
                    + ", value:'";

            if (hoverValue.contains("\n")) { // Support line breaks via show_item.
                String[] split = hoverValue.split("\\n");
                StringBuilder sb = new StringBuilder();

                for (int i = 1; i < split.length; ++i) {
                    if (split[i].contains(":")) // Fix a super-annoying bug in the JSON parser Mojang uses
                        sb.append("\", a:\"");
                    else sb.append("\", \"");


                    sb.append("\\u00A7f").append(split[i].replaceAll("\"", "\\\\\"").replaceAll(",",
                            "\\\\,").replaceAll("'", "\\'"));
                }

                // DRAWBACK: F3+H will show (#0001) beside the first line.
                element += "{id:1, tag:{display:{Name:\"\\u00A7f" + split[0] + "\", "
                        + (sb.toString().isEmpty() ? "" : "Lore:[" + sb.toString().substring(3) +
                        "\"]") + "}}}'}";
            } else {
                element += hoverValue.replaceAll("'", "\\\\'") + "'}";
            }

            elements.add(element);
        }

        boolean first = true;
        StringBuilder builder = new StringBuilder(result);
        for (String element : elements) {
            if (!first)
                builder.append(", ");
            builder.append(element);
            first = false;
        }

        return (builder + (brackets ? "}" : "")).replaceAll("\u00A7", "\\\\u00A7"); // Support for command blocks
    }

}