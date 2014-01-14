package org.futuredev.workbench.command.doc;

import org.futuredev.workbench.localisation.json.SpecialMessage;

public class HelpElement {

    String value, permission;

    public HelpElement (String value) {
        this.value = value;
    }

    public HelpElement append (String append) {
        this.value += append;
        return this;
    }

    public HelpElement setPermission (String node) {
        this.permission = node;
        return this;
    }

    public SpecialMessage construct () {
        return new SpecialMessage(this.value);
    }

}