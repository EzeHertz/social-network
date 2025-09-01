package com.ezehertz.socialnetwork.app.timeline;

import com.ezehertz.socialnetwork.app.cqbus.CommandHandler;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.timeline.TimelineEntry;
import com.ezehertz.socialnetwork.domain.timeline.TimelineStore;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.tweets.TweetRepository;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserNotFoundError;
import com.ezehertz.socialnetwork.domain.users.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetUserTimelineHandler implements CommandHandler<GetUserTimeline, GetUserTimelineResult> {
    private final RepositoryFactory repositories;
    private final TimelineStore timelines;

    public GetUserTimelineHandler(RepositoryFactory repositories, TimelineStore timelines) {
        this.repositories = repositories;
        this.timelines = timelines;
    }

    @Override
    public GetUserTimelineResult handle(GetUserTimeline cmd) {
        validateIfUserExists(cmd.userId());
        TweetRepository tweetRepository = repositories.create(TweetRepository.class);
        var page = timelines.range(cmd.userId(), cmd.limit(), cmd.cursorCreatedAt());
        var ids = page.entries().stream().map(TimelineEntry::tweetId).toList();
        Map<Id<Tweet>, Tweet> map = tweetRepository.findByIds(ids);
        return new GetUserTimelineResult(page.entries(), page.nextCursor(), map);
    }

    private void validateIfUserExists(Id<User> userId) {
        UserRepository users = repositories.create(UserRepository.class);
        if (users.findById(userId).isEmpty()) throw new UserNotFoundError("User with id " + userId + " not found");
    }

    @Override
    public Class<GetUserTimeline> commandType() {
        return GetUserTimeline.class;
    }
}
