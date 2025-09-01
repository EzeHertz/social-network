package com.ezehertz.socialnetwork.http.follow;

import com.ezehertz.socialnetwork.app.cqbus.CqBus;
import com.ezehertz.socialnetwork.app.follows.FollowUser;
import com.ezehertz.socialnetwork.app.follows.UnfollowUser;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{followerId}/follow/{followeeId}")
public class FollowController {
    private final CqBus bus;


    public FollowController(CqBus bus) {
        this.bus = bus;
    }


    @PostMapping
    public ResponseEntity<?> doFollow(@PathVariable String followerId, @PathVariable String followeeId) {
        bus.execute(new FollowUser(Id.of(followerId),Id.of(followeeId)));
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping
    public ResponseEntity<?> doUnfollow(@PathVariable String followerId, @PathVariable String followeeId) {
        bus.execute(new UnfollowUser(Id.of(followerId),Id.of(followeeId)));
        return ResponseEntity.noContent().build();
    }
}
