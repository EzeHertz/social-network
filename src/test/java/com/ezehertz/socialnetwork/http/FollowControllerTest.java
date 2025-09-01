package com.ezehertz.socialnetwork.http;

import com.ezehertz.socialnetwork.app.cqbus.CqBus;
import com.ezehertz.socialnetwork.app.follows.FollowUser;
import com.ezehertz.socialnetwork.app.follows.UnfollowUser;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.http.follow.FollowController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FollowControllerTest {

    private CqBus bus;
    private FollowController controller;

    @BeforeEach
    void setUp() {
        bus = mock(CqBus.class);
        controller = new FollowController(bus);
    }

    @Test
    void doFollowShouldPassCorrectCommandToBus() {
        String followerId = "U1";
        String followeeId = "U2";

        controller.doFollow(followerId, followeeId);

        verify(bus).execute(new FollowUser(Id.of(followerId), Id.of(followeeId)));
    }

    @Test
    void doUnfollowShouldPassCorrectCommandToBus() {
        String followerId = "U1";
        String followeeId = "U2";

        controller.doUnfollow(followerId, followeeId);

        verify(bus).execute(new UnfollowUser(Id.of(followerId), Id.of(followeeId)));
    }
}
