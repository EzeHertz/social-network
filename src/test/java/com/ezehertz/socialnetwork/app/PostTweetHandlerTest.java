package com.ezehertz.socialnetwork.app;

import com.ezehertz.socialnetwork.app.tweets.PostTweet;
import com.ezehertz.socialnetwork.app.tweets.PostTweetHandler;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.id.IdGenerator;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.tweets.TweetPublished;
import com.ezehertz.socialnetwork.domain.tweets.TweetRepository;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserNotFoundError;
import com.ezehertz.socialnetwork.domain.users.UserRepository;
import com.ezehertz.socialnetwork.infrastructure.inMemory.InMemoryRepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PostTweetHandlerTest {

    private IdGenerator ids;
    private ApplicationEventPublisher eventBus;
    private PostTweetHandler handler;
    private RepositoryFactory repos;

    @Test
    void shouldThrowUserNotFoundErrorIfUserDoesntExist() {
        Id<User> unexistenUserId = Id.of("U1");
        PostTweet cmd = new PostTweet(unexistenUserId, "hola mundo");

        assertThrows(UserNotFoundError.class, () -> handler.handle(cmd));
    }

    @Test
    void shouldSaveTweet() {
        var tweets = repos.create(TweetRepository.class);
        var users = repos.create(UserRepository.class);
        users.save(User.create(Id.of("U1"), "user", "user", 1L));
        PostTweet cmd = new PostTweet(Id.of("U1"), "hola mundo");

        Id<Tweet> id = handler.handle(cmd);

        assertThat(id.rawId()).isEqualTo("T-1");
        assertThat(tweets.findById(id).get().content()).isEqualTo("hola mundo");
    }

    @Test
    void shouldPublishTweetPublishedEvent() {
        var users = repos.create(UserRepository.class);
        users.save(User.create(Id.of("U1"), "user", "user", 1L));
        ArgumentCaptor<TweetPublished> eventCaptor = ArgumentCaptor.forClass(TweetPublished.class);
        PostTweet cmd = new PostTweet(Id.of("U1"), "hola mundo");

        handler.handle(cmd);

        verify(eventBus).publishEvent(eventCaptor.capture());
    }

    @BeforeEach
    void setUp() {
        ids = () -> "T-1";
        eventBus = mock(ApplicationEventPublisher.class);
        repos = new InMemoryRepositoryFactory();
        handler = new PostTweetHandler(repos, ids, eventBus);
    }
}
