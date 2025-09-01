package com.ezehertz.socialnetwork.app.follows;

import com.ezehertz.socialnetwork.app.cqbus.CommandHandler;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import org.springframework.stereotype.Component;

@Component
public class UnfollowUserHandler implements CommandHandler<UnfollowUser, Void> {
    private final RepositoryFactory repositories;

    public UnfollowUserHandler(RepositoryFactory repositories) {
        this.repositories = repositories;
    }

    @Override
    public Void handle(UnfollowUser cmd) {
        FollowRepository follows = repositories.create(FollowRepository.class);
        follows.unfollow(cmd.follower(), cmd.followee());
        return null;
    }

    @Override
    public Class<UnfollowUser> commandType() {
        return UnfollowUser.class;
    }
}
