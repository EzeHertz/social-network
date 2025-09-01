package com.ezehertz.socialnetwork.app.cqbus;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CqBus {
    private final Map<Class<?>, CommandHandler<?, ?>> commandHandlers = new ConcurrentHashMap<>();
    private final Map<Class<?>, QueryHandler<?, ?>> queryHandlers = new ConcurrentHashMap<>();

    public <C extends Command<R>, R> void register(CommandHandler<C, R> handler) {
        commandHandlers.put(handler.commandType(), handler);
    }

    public <Q extends Query<R>, R> void register(QueryHandler<Q, R> handler) {
        queryHandlers.put(handler.queryType(), handler);
    }

    @SuppressWarnings("unchecked")
    public <R, C extends Command<R>> R execute(C command) {
        var handler = (CommandHandler<C, R>) commandHandlers.get(command.getClass());
        if (handler == null) throw new RuntimeException("No handler for " + command.getClass());
        return handler.handle(command);
    }

    @SuppressWarnings("unchecked")
    public <R, Q extends Query<R>> R ask(Q query) {
        var handler = (QueryHandler<Q, R>) queryHandlers.get(query.getClass());
        if (handler == null) throw new RuntimeException("No handler for " + query.getClass());
        return handler.handle(query);
    }
}
