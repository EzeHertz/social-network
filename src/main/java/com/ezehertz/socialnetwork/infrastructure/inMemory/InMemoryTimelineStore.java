package com.ezehertz.socialnetwork.infrastructure.inMemory;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.timeline.TimelineEntry;
import com.ezehertz.socialnetwork.domain.timeline.TimelineStore;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.users.User;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTimelineStore implements TimelineStore {
    private final Map<String, List<TimelineEntry>> byUser = new HashMap<>();
    private final List<TimelineEntry> celebrityTimeline = new ArrayList<>();

    @Override
    public void pushToUserTimeline(Id<User> userId, long createdAt, Id<Tweet> tweetId, Id<User> authorId) {
        byUser.computeIfAbsent(userId.rawId(), k -> new ArrayList<>())
                .add(new TimelineEntry(userId, tweetId, authorId, createdAt));
    }

    @Override
    public void pushToCelebrityTimeline(long createdAt, Id<Tweet> tweetId, Id<User> authorId) {
        celebrityTimeline.add(new TimelineEntry(null, tweetId, authorId, createdAt));
    }

    @Override
    public TimelinePage range(Id<User> userId, int limit, Long cursorCreatedAt) {
        List<TimelineEntry> personal = byUser.getOrDefault(userId.rawId(), Collections.emptyList());
        List<TimelineEntry> celebrity = new ArrayList<>(celebrityTimeline);
        List<TimelineEntry> merged = new ArrayList<>();

        merged.addAll(personal);
        merged.addAll(celebrity);
        merged = merged.stream()
                .sorted(Comparator.comparingLong(TimelineEntry::createdAt).reversed())
                .collect(Collectors.toList());

        if (cursorCreatedAt != null) {
            merged = merged.stream()
                    .filter(e -> e.createdAt() < cursorCreatedAt)
                    .collect(Collectors.toList());
        }
        List<TimelineEntry> page = merged.stream().limit(limit).toList();
        Long nextCursor = page.size() == limit ? page.get(page.size() - 1).createdAt() : null;

        return new TimelinePage(page, nextCursor);
    }

    public List<TimelineEntry> getUserTimeline(Id<User> userId) {
        return byUser.getOrDefault(userId.rawId(), Collections.emptyList());
    }

    public List<TimelineEntry> getCelebrityTimeline() {
        return celebrityTimeline;
    }
}
