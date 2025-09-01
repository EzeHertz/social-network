package com.ezehertz.socialnetwork.infrastructure.inMemory;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.tweets.TweetRepository;
import com.ezehertz.socialnetwork.domain.users.User;

import java.util.*;

public class InMemoryTweets implements TweetRepository {
    public Map<String, Tweet> storage = new HashMap<>();

    @Override
    public void save(Tweet tweet) {
        storage.put(tweet.id().rawId(), tweet);
    }

    @Override
    public Optional<Tweet> findById(Id<Tweet> id) {
        return Optional.ofNullable(storage.get(id.rawId()));
    }

    @Override
    public Map<Id<Tweet>, Tweet> findByIds(Collection<Id<Tweet>> ids) {
        Map<Id<Tweet>, Tweet> m = new HashMap<>();
        for (var id : ids) {
            var t = storage.get(id.rawId());
            if (t != null) m.put(id, t);
        }
        return m;
    }

    @Override
    public List<Tweet> findByAuthor(Id<User> authorId, int limit, String cursor) {
        return List.of();
    }
}
