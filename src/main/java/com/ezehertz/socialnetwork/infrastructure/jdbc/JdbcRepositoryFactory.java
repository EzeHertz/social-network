package com.ezehertz.socialnetwork.infrastructure.jdbc;

import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import com.ezehertz.socialnetwork.domain.tweets.TweetRepository;
import com.ezehertz.socialnetwork.domain.users.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class JdbcRepositoryFactory implements RepositoryFactory {
    private final Map<Class<?>, Object> cache = new HashMap<>();
    private final JdbcTemplate jdbc;

    public JdbcRepositoryFactory(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> repositoryClass) {
        return (T) cache.computeIfAbsent(repositoryClass, cls -> {
            if (repositoryClass.equals(UserRepository.class)) {
                return (T) new JdbcUserRepository(jdbc);
            }
            if (repositoryClass.equals(TweetRepository.class)) {
                return (T) new JdbcTweetRepository(jdbc);
            }
            if (repositoryClass.equals(FollowRepository.class)) {
                return (T) new JdbcFollowRepository(jdbc);
            }
            throw new IllegalArgumentException("Unsupported repository: " + repositoryClass);
       });
    }
}
