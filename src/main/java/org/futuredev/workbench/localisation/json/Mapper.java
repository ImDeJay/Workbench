package org.futuredev.workbench.localisation.json;

import org.futuredev.workbench.localisation.json.node.Node;

import java.util.ArrayList;

public abstract class Mapper {

    ArrayList<Node> elements;
    int index;

    public Mapper () {
        elements = new ArrayList<Node>();
    }

    public ArrayList<Node> getElements () { return this.elements; }

}