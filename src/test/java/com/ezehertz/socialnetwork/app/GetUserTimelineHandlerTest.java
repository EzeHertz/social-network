package com.ezehertz.socialnetwork.app;

import com.ezehertz.socialnetwork.app.timeline.GetUserTimeline;
import com.ezehertz.socialnetwork.app.timeline.GetUserTimelineHandler;
import com.ezehertz.socialnetwork.app.timeline.GetUserTimelineResult;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.timeline.TimelineStore;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserNotFoundError;
import com.ezehertz.socialnetwork.helpers.TimelineTestHelper;
import com.ezehertz.socialnetwork.infrastructure.inMemory.InMemoryRepositoryFactory;
import com.ezehertz.socialnetwork.infrastructure.inMemory.InMemoryTimelineStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetUserTimelineHandlerTest {
    private RepositoryFactory factory;
    private TimelineStore timelineStore;
    private GetUserTimelineHandler handler;
    private TimelineTestHelper helper;

    @Test
    void shouldThrowUserNotFoundErrorIfUserDoesntExist() {
        Id<User> unexistenUserId = Id.of("U1");
        GetUserTimeline cmd = new GetUserTimeline(unexistenUserId, 1, null);

        assertThrows(UserNotFoundError.class, () -> handler.handle(cmd));
    }

    @Test
    void handle_shouldReturnTimelineWithTweets() {
        User user = helper.createUser("u1", "someUser");
        User author = helper.createUser("author1", "author");
        Tweet tweet = helper.createTweet("t1", author.id().rawId(), "hello world");

        helper.addToTimeline(user, tweet, author);

        GetUserTimeline cmd = new GetUserTimeline(user.id(), 10, null);
        GetUserTimelineResult result = handler.handle(cmd);

        assertThat(result.entries()).hasSize(1);
        assertThat(result.entries().get(0).tweetId()).isEqualTo(tweet.id());
        assertThat(result.tweetsById()).containsEntry(tweet.id(), tweet);
    }

    @Test
    void handle_shouldReturnEmptyWhenNoEntries() {
        User user = helper.createUser("u2", "someUser");

        GetUserTimeline cmd = new GetUserTimeline(user.id(), 10, null);
        GetUserTimelineResult result = handler.handle(cmd);

        assertThat(result.entries()).isEmpty();
        assertThat(result.tweetsById()).isEmpty();
        assertThat(result.nextCursor()).isNull();
    }

    @BeforeEach
    void setUp() {
        timelineStore = new InMemoryTimelineStore();
        factory = new InMemoryRepositoryFactory();
        handler = new GetUserTimelineHandler(factory, timelineStore);
        helper = new TimelineTestHelper(factory, timelineStore);
    }
}
