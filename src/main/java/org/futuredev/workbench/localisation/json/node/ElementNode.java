package org.futuredev.workbench.localisation.json.node;

import org.futuredev.workbench.localisation.json.Tag;

public class ElementNode extends Node {

    Tag tag;
    boolean close;

    public ElementNode (Tag tag, String value, int ID) {
        this (tag, value, ID, false);
    }

    public ElementNode (Tag tag, String value, int ID, boolean closing) {
        super(value, ID);
        this.tag   = tag;
        this.close = closing;
    }

    public boolean isOpening () { return !this.close; }

    public Tag getType () { return this.tag; }

}