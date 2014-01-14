package org.futuredev.workbench;

import org.bukkit.plugin.java.JavaPlugin;
import org.futuredev.workbench.data.EntityDatabase;
import org.futuredev.workbench.data.MaterialDatabase;
import org.futuredev.workbench.localisation.MessageAssist;
import org.futuredev.workbench.session.SessionManager;

import java.io.File;

/**
 * This is a Bukkit plugin which has the enhancements offered
 * by Workbench. It is highly recommended to use this class;
 * this ensures that Workbench does not encounter any issues
 * when performing various actions.
 *
 * @author afistofirony
 */
public abstract class AcceleratedPlugin extends JavaPlugin {

    private static AcceleratedPlugin instance;

    protected CommandManager manager;
    protected MaterialDatabase materials;
    protected EntityDatabase entities;
    protected SessionManager sessions;
    protected MessageAssist defLanguage;

    public static AcceleratedPlugin getInstance () {
        return instance;
    }

    public AcceleratedPlugin () {
        super();

        this.manager = new CommandManager();
        this.materials = new MaterialDatabase(new File(this.getDataFolder(), "materials.yml"));
        this.entities = new EntityDatabase(new File(this.getDataFolder(), "materials.yml"));
        this.sessions = new SessionManager(this);
        this.defLanguage = new MessageAssist(this, "English", "c78cc7", "messages-en_UK");

        instance = this;
    }

    public CommandManager getCommands () {
        return this.manager;
    }

    public MaterialDatabase getMaterials () {
        return this.materials;
    }

    public EntityDatabase getEntities () {
        return this.entities;
    }

    public SessionManager getSessions () { return this.sessions; }

}