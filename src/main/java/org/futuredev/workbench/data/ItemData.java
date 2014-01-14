package org.futuredev.workbench.data;

import org.bukkit.inventory.ItemStack;

public class ItemData {

    private int id, data;

    public ItemData (int id, int data) {
        this.id = id;
        this.data = data;
    }

    public int getID () { return id; }
    public int getData () { return data; }

    public ItemStack toStack () { return new ItemStack(id, 1, (short) data); }

    public static ItemData fromStack (ItemStack stack) {
        return new ItemData(stack.getTypeId(), stack.getDurability());
    }

}