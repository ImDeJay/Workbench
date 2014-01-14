package org.futuredev.workbench.command.doc;

import org.futuredev.workbench.localisation.json.SpecialMessage;

import java.util.ArrayList;

public class DynamicDocumentation {

    private static boolean generateJson = true;

    ArrayList<HelpElement> elements = new ArrayList<HelpElement>();
    HelpElement latest;

    public DynamicDocumentation (String value) {
        this.latest = new HelpElement(value);
    }

    public DynamicDocumentation then (String value) {
        this.elements.add(this.latest);
        this.latest = new HelpElement(value);
        return this;
    }

    public DynamicDocumentation withPermission (String permission) {
        this.latest.setPermission(permission);
        return this;
    }

    public String[] compile () {
        return null;
    }

    public SpecialMessage[] compileJson () {
        return null;
    }

    public static boolean shouldGenerateJson () { return generateJson; }

    public static void toggleJsonGeneration () {
        generateJson = !generateJson;
    }

}