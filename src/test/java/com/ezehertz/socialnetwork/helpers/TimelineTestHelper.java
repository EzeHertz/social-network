package com.ezehertz.socialnetwork.helpers;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.timeline.TimelineStore;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.tweets.TweetRepository;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserRepository;

public class TimelineTestHelper {
    private final RepositoryFactory factory;
    private final TimelineStore timelineStore;

    public TimelineTestHelper(RepositoryFactory factory, TimelineStore timelineStore) {
        this.factory = factory;
        this.timelineStore = timelineStore;
    }

    public User createUser(String id, String username) {
        Id<User> userId = Id.of(id);
        User user = User.create(userId, username, "pwd", System.currentTimeMillis());
        factory.create(UserRepository.class).save(user);
        return user;
    }

    public Tweet createTweet(String id, String authorId, String content) {
        Id<Tweet> tweetId = Id.of(id);
        Id<User> author = Id.of(authorId);
        Tweet tweet = Tweet.create(tweetId, author, content, System.currentTimeMillis());
        factory.create(TweetRepository.class).save(tweet);
        return tweet;
    }

    public void addToTimeline(User user, Tweet tweet, User author) {
        timelineStore.pushToUserTimeline(
                user.id(),
                tweet.createdAt(),
                tweet.id(),
                author.id()
        );
    }
}
