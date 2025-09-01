package com.ezehertz.socialnetwork.domain.follow;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.User;

import java.util.List;

public interface FollowRepository {
    void follow(Id<User> follower, Id<User> followee);
    void unfollow(Id<User> follower, Id<User> followee);
    List<Id<User>> findFollowerIds(Id<User> authorId);
    long countFollowers(Id<User> authorId);
}
