package com.ezehertz.socialnetwork.app.cqbus;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CqBusRegistrar {
    private final CqBus bus;
    private final List<CommandHandler<?, ?>> commandHandlers;
    private final List<QueryHandler<?, ?>> queryHandlers;

    public CqBusRegistrar(
        CqBus bus,
        List<CommandHandler<?, ?>> commandHandlers,
        List<QueryHandler<?, ?>> queryHandlers)
    {
        this.bus = bus;
        this.commandHandlers = commandHandlers;
        this.queryHandlers = queryHandlers;
    }

    @PostConstruct
    void init() {
        commandHandlers.forEach(bus::register);
        queryHandlers.forEach(bus::register);
    }
}
