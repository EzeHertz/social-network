package com.ezehertz.socialnetwork.app;

import com.ezehertz.socialnetwork.app.timeline.TimelineProjector;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import com.ezehertz.socialnetwork.domain.timeline.TimelineMetrics;
import com.ezehertz.socialnetwork.domain.tweets.TweetPublished;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.helpers.TimelineTestHelper;
import com.ezehertz.socialnetwork.infrastructure.inMemory.InMemoryRepositoryFactory;
import com.ezehertz.socialnetwork.infrastructure.inMemory.InMemoryTimelineStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
class TimelineProjectorTest {

    private RepositoryFactory factory;
    private InMemoryTimelineStore timeline;
    private FollowRepository follows;
    private FakeMetrics metrics;
    private TimelineProjector projector;
    private TimelineTestHelper helper;

    @Test
    void shouldPushToFollowersTimelineAndIncrementMetrics() {
        var author = helper.createUser("author1", "author1");
        var follower1 = helper.createUser("f1", "f1");
        var follower2 = helper.createUser("f2", "f2");

        follows.follow(follower1.id(), author.id());
        follows.follow(follower2.id(), author.id());
        TweetPublished event = new TweetPublished(Id.of("t-celeb"), author.id(), System.currentTimeMillis());

        projector.on(event);

        assertThat(timeline.getUserTimeline(follower1.id())).hasSize(1);
        assertThat(timeline.getUserTimeline(follower2.id())).hasSize(1);
        assertThat(metrics.fanoutCount).isEqualTo(2);
    }

    @Test
    void shouldPushToCelebrityTimelineIfCelebrity() {
        Id<User> celeb = Id.of("celeb1");
        for (int i = 0; i < 100_000; i++) {
            follows.follow(Id.of("f" + i), celeb);
        }
        TweetPublished event = new TweetPublished(Id.of("t-celeb"), celeb, System.currentTimeMillis());

        projector.on(event);

        assertThat(timeline.getCelebrityTimeline()).hasSize(1);
        assertThat(metrics.fanoutCount).isEqualTo(0);
    }

    @Test
    void shouldDoNothingWhenNoFollowers() {
        Id<User> lonely = Id.of("lonely");
        TweetPublished event = new TweetPublished(Id.of("t-lonely"), lonely, System.currentTimeMillis());

        projector.on(event);

        assertThat(timeline.getCelebrityTimeline()).isEmpty();
        assertThat(metrics.fanoutCount).isZero();
    }


    @BeforeEach
    void setUp() {
        this.timeline = new InMemoryTimelineStore();
        this.factory = new InMemoryRepositoryFactory();
        this.follows = factory.create(FollowRepository.class);
        this.metrics = new FakeMetrics();
        this.projector = new TimelineProjector(factory, timeline, f -> f >= 50_000, metrics);
        helper = new TimelineTestHelper(factory, timeline);
    }

    private static class FakeMetrics implements TimelineMetrics {
        int fanoutCount = 0;

        @Override
        public void incrementFanout() {
            fanoutCount++;
        }
    }
}
