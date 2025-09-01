package com.ezehertz.socialnetwork.domain.timeline;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.users.User;

public record TimelineEntry(Id<User> userId, Id<Tweet> tweetId, Id<User> authorId, long createdAt) { }
