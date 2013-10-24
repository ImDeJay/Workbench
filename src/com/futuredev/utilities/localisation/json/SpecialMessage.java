package com.futuredev.utilities.localisation.json;

import com.futuredev.utilities.localisation.json.node.ElementNode;
import com.futuredev.utilities.localisation.json.node.Node;
import com.futuredev.utilities.localisation.json.node.StringNode;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialMessage {

    ArrayList<Element> result;
    String output;

    public SpecialMessage (String input) {

        this.result = new ArrayList<Element>();
        this.output = "";

        ArrayList<Node> nodes = new ElementMapper(input).getElements();

        for (int i = 0; i < nodes.size(); ++i) {

            Node node = nodes.get(i);

            if (node instanceof StringNode) { // Is it a String node?
                Element add = new Element();

                HashMap<Tag, Node> tags = getOpenElements(nodes, i);
                add.setValue(node.getValue());

                if (tags.containsKey(Tag.REF))
                    add.setClickAction(Action.RUN_COMMAND, tags.get(Tag.REF).getValue());

                if (tags.containsKey(Tag.HREF))
                    add.setClickAction(Action.OPEN_URL, tags.get(Tag.HREF).getValue());

                if (tags.containsKey(Tag.TIP))
                    add.setHoverValue(tags.get(Tag.TIP).getValue());

                if (tags.containsKey(Tag.PROMPT))
                    add.setClickAction(Action.SUGGEST_COMMAND, tags.get(Tag.PROMPT).getValue());

                if (tags.containsKey(Tag.STRONG))
                    add.setBold(true);

                if (tags.containsKey(Tag.EMPHASIS))
                    add.setItalic(true);

                if (tags.containsKey(Tag.UNDERLINE))
                    add.setUnderline(true);

                if (tags.containsKey(Tag.STRIKE))
                    add.setStrike(true);

                if (tags.containsKey(Tag.COLOUR))
                    add.setColour(ColorPattern.fromString(tags.get(Tag.COLOUR).getValue()).pattern);

                if (tags.containsKey(Tag.RESET)) {

                    if (tags.containsKey(Tag.STRONG))
                        add.setBold(tags.get(Tag.RESET).getID() < tags.get(Tag.STRONG).getID());

                    if (tags.containsKey(Tag.EMPHASIS))
                        add.setItalic(tags.get(Tag.RESET).getID() < tags.get(Tag.EMPHASIS).getID());

                    if (tags.containsKey(Tag.UNDERLINE))
                        add.setUnderline(tags.get(Tag.RESET).getID() < tags.get(Tag.UNDERLINE).getID());

                    if (tags.containsKey(Tag.STRIKE))
                        add.setStrike(tags.get(Tag.RESET).getID() < tags.get(Tag.STRIKE).getID());

                    if (tags.containsKey(Tag.MAGIC))
                        add.setMagic(tags.get(Tag.RESET).getID() < tags.get(Tag.MAGIC).getID());

                    if (tags.containsKey(Tag.COLOUR))
                        add.setColour(tags.get(Tag.RESET).getID() < tags.get(Tag.COLOUR).getID() ?
                                ColorPattern.fromString(tags.get(Tag.COLOUR).getValue()).pattern
                                : new ChatColor[] { ChatColor.WHITE });
                }

                result.add(add);
            }

        }

    }

    public String toString () {
        String result = "";

        if (this.result.size() > 1) {
            result = ", extra:[";
            for (int i = 1; i < this.result.size(); ++i) {
                Element element = this.result.get(i);
                result += (i > 1 ? ", " : "") + element.toString(true);
            }
            result += "]";
        }

        if (this.result.size() < 1)
            return "{}";

        return "{" + this.result.get(0).toString(false) + result + "}";
    }

    private HashMap<Tag, Node> getOpenElements (ArrayList<Node> nodes, int backFrom) {
        ArrayList<Integer> closedTags = new ArrayList<Integer>();
        HashMap<Tag, Node> result = new HashMap<Tag, Node>();

        if (backFrom >= nodes.size())
            backFrom = nodes.size() - 1;

        for (int i = backFrom; i >= 0; --i) {
            if (nodes.get(i) instanceof ElementNode) {
                ElementNode node = (ElementNode) nodes.get(i);
                if (result.containsKey(node.getType()))
                    continue;

                if (!closedTags.contains(node.getID())) {
                    if (node.isOpening()) {
                        result.put(node.getType(), node);
                  } else {
                        closedTags.add(node.getID());
                    }
                }
            }
        }

        return result;
    }

}