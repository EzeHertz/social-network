package com.ezehertz.socialnetwork.app.follows;

import com.ezehertz.socialnetwork.app.cqbus.CommandHandler;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import org.springframework.stereotype.Component;

@Component
public class FollowUserHandler implements CommandHandler<FollowUser, Void> {
    private final RepositoryFactory repositories;

    public FollowUserHandler(RepositoryFactory repositories) {
        this.repositories = repositories;
    }

    @Override
    public Void handle(FollowUser cmd) {
        FollowRepository follows = repositories.create(FollowRepository.class);
        follows.follow(cmd.follower(), cmd.followee());
        return null;
    }

    @Override
    public Class<FollowUser> commandType() {
        return FollowUser.class;
    }
}
