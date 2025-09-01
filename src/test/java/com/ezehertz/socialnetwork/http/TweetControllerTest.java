package com.ezehertz.socialnetwork.http;

import com.ezehertz.socialnetwork.app.cqbus.CqBus;
import com.ezehertz.socialnetwork.app.tweets.PostTweet;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.tweets.InvalidTweetError;
import com.ezehertz.socialnetwork.domain.users.UserNotFoundError;
import com.ezehertz.socialnetwork.http.tweets.TweetController;
import com.ezehertz.socialnetwork.http.tweets.TweetRequest;
import com.ezehertz.socialnetwork.http.tweets.TweetResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TweetControllerTest {
    private CqBus bus;
    private TweetController controller;

    @Test
    void shouldPassCorrectCommandToBus() {
        var req = new TweetRequest("U2", "my first tweet");
        var tweetId = Id.of("T1");
        when(bus.execute(any())).thenReturn(tweetId);

        controller.post(req);

        verify(bus).execute(new PostTweet(Id.of("U2"), "my first tweet"));
    }

    @Test
    void shouldCreateTweetWhenValidRequest() {
        var req = new TweetRequest("U1", "hello world!");
        var tweetId = Id.of("T1");
        when(bus.execute(any())).thenReturn(tweetId);

        var response = controller.post(req);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(TweetResponse.class);
        var body = (TweetResponse) response.getBody();
        assertThat(body.id()).isEqualTo("T1");
        assertThat(body.userId()).isEqualTo("U1");
        assertThat(body.content()).isEqualTo("hello world!");
    }

    @Test
    void shouldReturnBadRequestWhenTweetIsInvalid() {
        var req = new TweetRequest("U1", "hello world!");
        when(bus.execute(any(PostTweet.class))).thenThrow(new InvalidTweetError("Content cannot be empty"));

        var response = controller.post(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody())
                .isInstanceOf(Map.class)
                .satisfies(body -> {
                    Map<?, ?> map = (Map<?, ?>) body;
                    assertThat(map.get("error")).isEqualTo("invalid_tweet");
                    assertThat(map.get("message")).isEqualTo("Content cannot be empty");
                });
    }

    @Test
    void shouldReturnNotFoundErrorWhenAuthorIdNotFound() {
        var req = new TweetRequest("U1", "hello world!");
        when(bus.execute(any(PostTweet.class))).thenThrow(new UserNotFoundError("User with id U1 not found"));

        var response = controller.post(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody())
                .isInstanceOf(Map.class)
                .satisfies(body -> {
                    Map<?, ?> map = (Map<?, ?>) body;
                    assertThat(map.get("error")).isEqualTo("user_not_found");
                    assertThat(map.get("message")).isEqualTo("User with id U1 not found");
                });
    }

    @BeforeEach
    void setUp() {
        bus = mock(CqBus.class);
        controller = new TweetController(bus);
    }
}
