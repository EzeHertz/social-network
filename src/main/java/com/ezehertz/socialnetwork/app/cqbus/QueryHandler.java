package com.ezehertz.socialnetwork.app.cqbus;

public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
    Class<Q> queryType();
}
