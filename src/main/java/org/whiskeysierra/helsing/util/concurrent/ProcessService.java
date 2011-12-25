package org.whiskeysierra.helsing.util.concurrent;

import java.io.File;

public interface ProcessService {

    ManagedProcess prepare(File script, Object... arguments);

    ManagedProcess prepare(File script, Iterable<?> arguments);

    ManagedProcess prepare(String command, Object... arguments);

    ManagedProcess prepare(String command, Iterable<?> arguments);

}
