package com.ezehertz.socialnetwork.infrastructure.inMemory;

import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import com.ezehertz.socialnetwork.domain.tweets.TweetRepository;
import com.ezehertz.socialnetwork.domain.users.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepositoryFactory implements RepositoryFactory {
    private final Map<Class<?>, Object> cache = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> repositoryClass) {
        return (T) cache.computeIfAbsent(repositoryClass, cls -> {
            if (cls.equals(UserRepository.class)) {
                return new InMemoryUsers();
            }
            if (cls.equals(TweetRepository.class)) {
                return new InMemoryTweets();
            }
            if (cls.equals(FollowRepository.class)) {
                return new InMemoryFollows();
            }
            throw new IllegalArgumentException("Unsupported repository: " + cls);
        });
    }
}
