package com.ezehertz.socialnetwork.http;

import com.ezehertz.socialnetwork.app.cqbus.CqBus;
import com.ezehertz.socialnetwork.app.timeline.GetUserTimeline;
import com.ezehertz.socialnetwork.app.timeline.GetUserTimelineResult;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.timeline.TimelineEntry;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserNotFoundError;
import com.ezehertz.socialnetwork.http.timeline.TimelineController;
import com.ezehertz.socialnetwork.http.timeline.TimelineResponse;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TimelineControllerTest {
    private CqBus bus;
    private TimelineController controller;

    @Test
    void shouldPassCorrectCommandToBus() {
        String userId = "U1";
        int limit = 5;
        when(bus.execute(any())).thenReturn(new GetUserTimelineResult(List.of(), null, Map.of()));

        controller.getTimeline(userId, limit, null);

        verify(bus).execute(new GetUserTimeline(Id.of(userId), limit, null));
    }

    @Test
    void shouldReturnMappedTimelineResponse() {
        String userId = "U3";
        Id<User> uid = Id.of(userId);
        Id<Tweet> tid = Id.of("T1");
        Id<User> authorId = Id.of("U2");

        TimelineEntry entry = new TimelineEntry(uid, tid, authorId, 111L);
        Tweet tweet = Tweet.create(tid, authorId, "hello world", 111L);

        GetUserTimelineResult result = new GetUserTimelineResult(
                List.of(entry),
                222L,
                Map.of(tid, tweet)
        );
        when(bus.execute(any())).thenReturn(result);

        var response = controller.getTimeline(userId, 10, null);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        var parsedResponse = (TimelineResponse) response.getBody();
        assertThat(parsedResponse.entries()).hasSize(1);
        assertThat(parsedResponse.entries().get(0).tweetId()).isEqualTo("T1");
        assertThat(parsedResponse.tweetsById()).containsKey("T1");
        assertThat(parsedResponse.tweetsById().get("T1").content()).isEqualTo("hello world");
        assertThat(parsedResponse.nextCursor()).isEqualTo(222L);
    }

    @Test
    void shouldReturnNotFoundErrorWhenAuthorIdNotFound() {
        when(bus.execute(any(GetUserTimeline.class))).thenThrow(new UserNotFoundError("User with id U1 not found"));

        var response = controller.getTimeline("U1", 1, null);

        AssertionsForClassTypes.assertThat(response.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(response.getBody())
                .isInstanceOf(Map.class)
                .satisfies(body -> {
                    Map<?, ?> map = (Map<?, ?>) body;
                    AssertionsForClassTypes.assertThat(map.get("error")).isEqualTo("user_not_found");
                    AssertionsForClassTypes.assertThat(map.get("message")).isEqualTo("User with id U1 not found");
                });
    }

    @BeforeEach
    void setUp() {
        bus = mock(CqBus.class);
        controller = new TimelineController(bus);
    }
}
