package com.ezehertz.socialnetwork.domain.tweets;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TweetRepository {
    void save(Tweet tweet);
    Optional<Tweet> findById(Id<Tweet> id);
    Map<Id<Tweet>, Tweet> findByIds(Collection<Id<Tweet>> ids);
    List<Tweet> findByAuthor(Id<User> authorId, int limit, String cursor);
}
