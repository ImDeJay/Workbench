package org.futuredev.workbench.data;

public class EntityData {

    private static EntityDatabase database = null;

    int id;

    public EntityData (int id) {
        this.id = id;
    }

    public int getID () {
        return id;
    }

    public String getName () {
        if (database == null)
            return null;
        return database.getDefaultName(id, 1);
    }

    public static void setDatabase (EntityDatabase db) {
        database = db;
    }

}