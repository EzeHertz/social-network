package com.ezehertz.socialnetwork.http.tweets;

import com.ezehertz.socialnetwork.domain.tweets.Tweet;

public record TweetResponse(
        String id,
        String userId,
        String content,
        long createdAt
) {
    public static TweetResponse fromTweet(Tweet tweet) {
        return new TweetResponse(
                tweet.id().rawId(),
                tweet.authorId().rawId(),
                tweet.content(),
                tweet.createdAt()
        );
    }
}
