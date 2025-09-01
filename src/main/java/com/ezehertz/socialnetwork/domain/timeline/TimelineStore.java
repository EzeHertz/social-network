package com.ezehertz.socialnetwork.domain.timeline;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.users.User;

import java.util.List;

public interface TimelineStore {
    void pushToUserTimeline(Id<User> userId, long createdAt, Id<Tweet> tweetId, Id<User> authorId);
    void pushToCelebrityTimeline(long createdAt, Id<Tweet> tweetId, Id<User> authorId);
    TimelinePage range(Id<User> userId, int limit, Long cursorCreatedAt);

    record TimelinePage(List<TimelineEntry> entries, Long nextCursor) {
    }
}
