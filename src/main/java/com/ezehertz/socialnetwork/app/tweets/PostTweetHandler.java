package com.ezehertz.socialnetwork.app.tweets;

import com.ezehertz.socialnetwork.app.cqbus.CommandHandler;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.id.IdGenerator;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.tweets.TweetPublished;
import com.ezehertz.socialnetwork.domain.tweets.TweetRepository;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserNotFoundError;
import com.ezehertz.socialnetwork.domain.users.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PostTweetHandler implements CommandHandler<PostTweet, Id<Tweet>> {
    private final RepositoryFactory repositories;
    private final IdGenerator idGenerator;
    private final ApplicationEventPublisher events;

    public PostTweetHandler(RepositoryFactory repositories, IdGenerator idGenerator, ApplicationEventPublisher events) {
        this.repositories = repositories;
        this.idGenerator = idGenerator;
        this.events = events;
    }

    @Override
    public Id<Tweet> handle(PostTweet cmd) {
        validateIfAuthorExists(cmd.authorId());
        TweetRepository tweets = repositories.create(TweetRepository.class);
        long now = System.currentTimeMillis();
        Id<Tweet> id = Id.of(idGenerator.newId());
        Tweet t = Tweet.create(id, cmd.authorId(), cmd.content(), now);
        tweets.save(t);
        events.publishEvent(new TweetPublished(id, cmd.authorId(), now));
        return id;
    }

    private void validateIfAuthorExists(Id<User> authorId) {
        UserRepository users = repositories.create(UserRepository.class);
        if (users.findById(authorId).isEmpty()) throw new UserNotFoundError("User with id " + authorId + " not found");
    }

    @Override
    public Class<PostTweet> commandType() {
        return PostTweet.class;
    }
}
