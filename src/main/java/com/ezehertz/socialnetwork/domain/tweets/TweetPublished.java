package com.ezehertz.socialnetwork.domain.tweets;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.User;

public record TweetPublished(Id<Tweet> tweetId, Id<User> authorId, long createdAt) { }
