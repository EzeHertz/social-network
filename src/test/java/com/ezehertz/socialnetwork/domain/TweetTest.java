package com.ezehertz.socialnetwork.domain;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.tweets.InvalidTweetError;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.users.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TweetTest {

    @Test
    void shouldCreateTweetWhen_dataIsValid() {
        Id<Tweet> id = Id.of("T1");
        Id<User> authorId = Id.of("U1");
        Tweet tweet = Tweet.create(id, authorId, "hello world", System.currentTimeMillis());

        assertThat(tweet).isNotNull();
        assertThat(tweet.id()).isEqualTo(id);
        assertThat(tweet.authorId()).isEqualTo(authorId);
        assertThat(tweet.content()).isEqualTo("hello world");
    }

    @Test
    void shouldThrow_errorWhenIdIsNull() {
        assertThatThrownBy(() -> Tweet.create(null, Id.of("U1"), "hello", System.currentTimeMillis()))
                .isInstanceOf(InvalidTweetError.class)
                .hasMessage("Tweet id cannot be null");
    }

    @Test
    void shouldThrow_errorWhen_authorIdIsNull() {
        assertThatThrownBy(() -> Tweet.create(Id.of("T1"), null, "hello", System.currentTimeMillis()))
                .isInstanceOf(InvalidTweetError.class)
                .hasMessage("Author id cannot be null");
    }

    @Test
    void shouldThrow_errorWhenContentIsNull() {
        assertThatThrownBy(() -> Tweet.create(Id.of("T1"), Id.of("U1"), null, System.currentTimeMillis()))
                .isInstanceOf(InvalidTweetError.class)
                .hasMessage("Content cannot be empty");
    }

    @Test
    void shouldThrow_errorWhenContentIsBlank() {
        assertThatThrownBy(() -> Tweet.create(Id.of("T1"), Id.of("U1"), "   ", System.currentTimeMillis()))
                .isInstanceOf(InvalidTweetError.class)
                .hasMessage("Content cannot be empty");
    }
}
