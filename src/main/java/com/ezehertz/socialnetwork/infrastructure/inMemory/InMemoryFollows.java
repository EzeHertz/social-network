package com.ezehertz.socialnetwork.infrastructure.inMemory;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import com.ezehertz.socialnetwork.domain.users.User;

import java.util.*;

public class InMemoryFollows implements FollowRepository {
    private final Map<String, Set<String>> followersByAuthor = new HashMap<>();

    @Override
    public void follow(Id<User> follower, Id<User> followee) {
        followersByAuthor
                .computeIfAbsent(followee.rawId(), k -> new HashSet<>())
                .add(follower.rawId());
    }

    @Override
    public void unfollow(Id<User> follower, Id<User> followee) {
        followersByAuthor
                .getOrDefault(followee.rawId(), Set.of())
                .remove(follower.rawId());
    }

    @Override
    public List<Id<User>> findFollowerIds(Id<User> authorId) {
        return followersByAuthor
                .getOrDefault(authorId.rawId(), Set.of())
                .stream()
                .map(Id::<User>of)
                .toList();
    }

    @Override
    public long countFollowers(Id<User> authorId) {
        return followersByAuthor
                .getOrDefault(authorId.rawId(), Set.of())
                .size();
    }
}
