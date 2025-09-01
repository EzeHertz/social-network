package com.ezehertz.socialnetwork.infrastructure.redis;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.timeline.TimelineEntry;
import com.ezehertz.socialnetwork.domain.timeline.TimelineStore;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.users.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class RedisTimelineStore implements TimelineStore {
    private final StringRedisTemplate redis;

    public RedisTimelineStore(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private static String kTimeline(Id<User> u) { return "timeline:" + u.rawId(); }
    private static String kCelebrityTimeline() { return "timeline:celebrities"; }
    private static String member(Id<Tweet> t, Id<User> a) { return t.rawId() + "|" + a.rawId(); }

    @Override
    public void pushToUserTimeline(Id<User> userId, long createdAt, Id<Tweet> tweetId, Id<User> authorId) {
        redis.opsForZSet().add(kTimeline(userId), member(tweetId, authorId), createdAt);
    }

    @Override
    public void pushToCelebrityTimeline(long createdAt, Id<Tweet> tweetId, Id<User> authorId) {
        redis.opsForZSet().add(kCelebrityTimeline(), member(tweetId, authorId), createdAt);
    }

    @Override
    public TimelinePage range(Id<User> userId, int limit, Long cursorCreatedAt) {
        double max = cursorCreatedAt == null ? Double.POSITIVE_INFINITY : (cursorCreatedAt.doubleValue() - 0.0001);

        Set<ZSetOperations.TypedTuple<String>> personalTuples = redis.opsForZSet()
                .reverseRangeByScoreWithScores(kTimeline(userId), Double.NEGATIVE_INFINITY, max, 0, limit);

        Set<ZSetOperations.TypedTuple<String>> celebTuples = redis.opsForZSet()
                .reverseRangeByScoreWithScores(kCelebrityTimeline(), Double.NEGATIVE_INFINITY, max, 0, limit);

        if (personalTuples == null) personalTuples = Set.of();
        if (celebTuples == null) celebTuples = Set.of();

        List<TimelineEntry> all = new ArrayList<>();

        for (var t : personalTuples) {
            String[] parts = t.getValue().split("\\|");
            String tweetId = parts[0];
            String authorId = parts.length > 1 ? parts[1] : "";
            long score = t.getScore().longValue();
            all.add(new TimelineEntry(userId, Id.of(tweetId), Id.of(authorId), score));
        }

        for (var t : celebTuples) {
            String[] parts = t.getValue().split("\\|");
            String tweetId = parts[0];
            String authorId = parts.length > 1 ? parts[1] : "";
            long score = t.getScore().longValue();
            all.add(new TimelineEntry(userId, Id.of(tweetId), Id.of(authorId), score));
        }

        all.sort((a, b) -> Long.compare(b.createdAt(), a.createdAt()));
        List<TimelineEntry> page = all.stream().limit(limit).toList();
        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).createdAt();

        return new TimelinePage(page, nextCursor);
    }

}
