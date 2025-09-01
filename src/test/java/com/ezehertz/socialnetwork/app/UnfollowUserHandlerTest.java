package com.ezehertz.socialnetwork.app;

import com.ezehertz.socialnetwork.app.follows.UnfollowUser;
import com.ezehertz.socialnetwork.app.follows.UnfollowUserHandler;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.infrastructure.inMemory.InMemoryRepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UnfollowUserHandlerTest {

    private RepositoryFactory factory;
    private UnfollowUserHandler handler;

    @Test
    void handleShouldCallRepositoryFollow() {
        var follows = factory.create(FollowRepository.class);
        Id<User> follower = Id.of("u1");
        Id<User> followee = Id.of("u2");
        follows.follow(follower, followee);
        UnfollowUser cmd = new UnfollowUser(follower, followee);

        handler.handle(cmd);

        var followerCount = follows.findFollowerIds(followee);
        assertThat(followerCount.size()).isEqualTo(0);
    }

    @BeforeEach
    void setUp() {
        factory = new InMemoryRepositoryFactory();
        handler = new UnfollowUserHandler(factory);
    }
}
