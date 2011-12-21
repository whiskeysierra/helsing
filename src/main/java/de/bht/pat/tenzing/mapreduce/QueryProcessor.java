package de.bht.pat.tenzing.mapreduce;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.bht.pat.tenzing.events.QueryEvent;

final class QueryProcessor {

    private EventBus bus;

    @Inject
    public QueryProcessor(EventBus bus) {
        this.bus = bus;

        bus.register(this);
    }

    @Subscribe
    public void onStatement(QueryEvent event) {
        // TODO call hadoop, wait for response, fire either success/result or error
    }

}
