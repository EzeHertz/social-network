package com.ezehertz.socialnetwork.domain.users;

import com.ezehertz.socialnetwork.domain.common.id.Id;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(Id<User> id);
    boolean existsByUsername(String username);
}
