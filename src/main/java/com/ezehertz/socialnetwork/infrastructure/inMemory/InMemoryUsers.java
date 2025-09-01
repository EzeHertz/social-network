package com.ezehertz.socialnetwork.infrastructure.inMemory;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUsers implements UserRepository {
    public Map<String, User> storage = new HashMap<>();

    @Override
    public void save(User user) {
        storage.put(user.id().rawId(), user);
    }

    @Override
    public Optional<User> findById(Id<User> id) {
        return Optional.ofNullable(storage.get(id.rawId()));
    }

    @Override
    public boolean existsByUsername(String username) {
        return storage.values().stream()
                .anyMatch(u -> u.username().equals(username));
    }
}
