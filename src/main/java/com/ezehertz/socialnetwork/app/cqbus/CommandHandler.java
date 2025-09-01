package com.ezehertz.socialnetwork.app.cqbus;

public interface CommandHandler<C extends Command<R>, R> {
    R handle(C command);
    Class<C> commandType();
}
