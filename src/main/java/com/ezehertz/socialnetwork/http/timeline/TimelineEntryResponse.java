package com.ezehertz.socialnetwork.http.timeline;

import com.ezehertz.socialnetwork.domain.timeline.TimelineEntry;

public record TimelineEntryResponse(
        String userId,
        String tweetId,
        String authorId,
        long createdAt
) {
    public static TimelineEntryResponse fromTimelineEntry(TimelineEntry entry) {
        return new TimelineEntryResponse(
                entry.userId().rawId(),
                entry.tweetId().rawId(),
                entry.authorId().rawId(),
                entry.createdAt()
        );
    }
}
