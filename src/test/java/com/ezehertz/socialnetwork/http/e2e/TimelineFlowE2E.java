package com.ezehertz.socialnetwork.http.e2e;

import com.ezehertz.socialnetwork.app.timeline.GetUserTimeline;
import com.ezehertz.socialnetwork.app.timeline.GetUserTimelineHandler;
import com.ezehertz.socialnetwork.app.timeline.TimelineProjector;
import com.ezehertz.socialnetwork.app.tweets.PostTweet;
import com.ezehertz.socialnetwork.app.tweets.PostTweetHandler;
import com.ezehertz.socialnetwork.app.users.CreateUser;
import com.ezehertz.socialnetwork.app.users.CreateUserHandler;
import com.ezehertz.socialnetwork.domain.common.id.IdGenerator;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.fanout.FanoutPolicy;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import com.ezehertz.socialnetwork.domain.timeline.TimelineMetrics;
import com.ezehertz.socialnetwork.domain.timeline.TimelineStore;
import com.ezehertz.socialnetwork.domain.tweets.TweetPublished;
import com.ezehertz.socialnetwork.infrastructure.fanout.DefaultFanoutPolicy;
import com.ezehertz.socialnetwork.infrastructure.idGenerator.UuidIdGenerator;
import com.ezehertz.socialnetwork.infrastructure.inMemory.InMemoryRepositoryFactory;
import com.ezehertz.socialnetwork.infrastructure.inMemory.InMemoryTimelineStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class TimelineFlowE2E {
    private RepositoryFactory factory;
    private TimelineStore timelines;
    private ApplicationEventPublisher events;
    private IdGenerator ids;
    private FanoutPolicy fanoutPolicy;
    private TimelineMetrics metrics;

    private CreateUserHandler createUserHandler;
    private PostTweetHandler postTweetHandler;
    private GetUserTimelineHandler getUserTimelineHandler;

    @Test
    void timelineShouldIncludeFollowedUsersTweets() {
        var follows = factory.create(FollowRepository.class);
        var aliceId = createUserHandler.handle(new CreateUser("alice", "alice123"));
        var bobId = createUserHandler.handle(new CreateUser("bob", "bob123"));
        follows.follow(aliceId, bobId);

        postTweetHandler.handle(new PostTweet(bobId, "hola desde bob"));

        var result = getUserTimelineHandler.handle(new GetUserTimeline(aliceId, 10, null));
        assertThat(result.entries()).hasSize(1);
        assertThat(result.tweetsById().values().iterator().next().content()).isEqualTo("hola desde bob");
    }

    @Test
    void timelineShouldBeEmptyWhenNoFollows() {
        var aliceId = createUserHandler.handle(new CreateUser("alice", "alice123"));

        var result = getUserTimelineHandler.handle(new GetUserTimeline(aliceId, 10, null));

        assertThat(result.entries()).isEmpty();
    }

    @BeforeEach
    void setUp() {
        timelines = new InMemoryTimelineStore();
        ids = new UuidIdGenerator();
        fanoutPolicy = new DefaultFanoutPolicy();
        metrics = mock(TimelineMetrics.class);
        factory = new InMemoryRepositoryFactory();
        events = evt -> {
            if (evt instanceof TweetPublished e) {
                new TimelineProjector(factory, timelines, fanoutPolicy, metrics)
                        .on(e);
            }
        };
        createUserHandler = new CreateUserHandler(factory, ids);
        postTweetHandler = new PostTweetHandler(factory, ids, events);
        getUserTimelineHandler = new GetUserTimelineHandler(factory, timelines);
    }
}
