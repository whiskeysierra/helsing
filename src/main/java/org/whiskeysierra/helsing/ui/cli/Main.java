package org.whiskeysierra.helsing.ui.cli;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.whiskeysierra.helsing.ApplicationModule;
import org.whiskeysierra.helsing.events.BootEvent;

import java.io.IOException;

public final class Main {

    public static void main(String[] args) throws IOException {
        final Injector injector = Guice.createInjector(
            new ApplicationModule(),
            new CommandLineInterfaceModule()
        );
        final EventBus bus = injector.getInstance(EventBus.class);
        bus.post(new BootEvent());
    }

}
