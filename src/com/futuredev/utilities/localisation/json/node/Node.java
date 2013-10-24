package com.futuredev.utilities.localisation.json.node;

public class Node {

    String value;
    int index;

    public Node (String value, int ID) {
        this.value = value;
        this.index = ID;
    }

    public int getID () { return index; }

    public String getValue () { return value; }

}