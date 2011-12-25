package org.whiskeysierra.helsing.ui;

import com.google.inject.AbstractModule;
import org.whiskeysierra.helsing.ui.cli.CommandLineInterfaceModule;

public final class UserInterfaceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new CommandLineInterfaceModule());
    }

}
