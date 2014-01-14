package org.futuredev.workbench.command.reflective;

import java.util.List;

public interface Completable {

    List<String> complete (String input);

}
