package com.ezehertz.socialnetwork.http.tweets;

import com.ezehertz.socialnetwork.app.cqbus.CqBus;
import com.ezehertz.socialnetwork.app.tweets.PostTweet;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.tweets.InvalidTweetError;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.users.UserNotFoundError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tweets")
public class TweetController {
    private final CqBus bus;

    public TweetController(CqBus bus) {
        this.bus = bus;
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody TweetRequest req) {
        try {
            System.out.println("authorId: " + req.authorId());
            Id<Tweet> id = bus.execute(new PostTweet(Id.of(req.authorId()), req.content()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new TweetResponse(id.rawId(), req.authorId(), req.content(), System.currentTimeMillis()));
        } catch (InvalidTweetError e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", "invalid_tweet",
                            "message", e.getMessage()
                    ));
        } catch (UserNotFoundError e) {
            System.out.println("UserNotFoundError: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "user_not_found",
                            "message", e.getMessage()
                    ));
        }
    }

}
