package de.bht.pat.tenzing;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.bht.pat.tenzing.events.BootEvent;

import java.io.IOException;

public final class Main {

    public static void main(String[] args) throws IOException {
        final MainModule module = new MainModule();
        final Injector injector = Guice.createInjector(module);
        final EventBus bus = injector.getInstance(EventBus.class);
        bus.post(new BootEvent());
    }

}
