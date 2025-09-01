package com.ezehertz.socialnetwork.app.timeline;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.fanout.FanoutPolicy;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import com.ezehertz.socialnetwork.domain.timeline.TimelineMetrics;
import com.ezehertz.socialnetwork.domain.timeline.TimelineStore;
import com.ezehertz.socialnetwork.domain.tweets.TweetPublished;
import com.ezehertz.socialnetwork.domain.users.User;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TimelineProjector {
    private final RepositoryFactory repositories;
    private final TimelineStore timeline;
    private final FanoutPolicy policy;
    private final TimelineMetrics metrics;

    public TimelineProjector(
            RepositoryFactory repositories,
            TimelineStore timeline,
            FanoutPolicy policy,
            TimelineMetrics metrics
    ) {
        this.repositories = repositories;
        this.timeline = timeline;
        this.policy = policy;
        this.metrics = metrics;
    }

    @EventListener
    public void on(TweetPublished e) {
        FollowRepository follows = repositories.create(FollowRepository.class);
        long followers = follows.countFollowers(e.authorId());
        if (policy.isCelebrity(followers)) {
            timeline.pushToCelebrityTimeline(e.createdAt(), e.tweetId(), e.authorId());
            return;
        }
        for (Id<User> u : follows.findFollowerIds(e.authorId())) {
            timeline.pushToUserTimeline(u, e.createdAt(), e.tweetId(), e.authorId());
            metrics.incrementFanout();
        }
    }
}
