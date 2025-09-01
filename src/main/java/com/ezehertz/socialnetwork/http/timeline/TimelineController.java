package com.ezehertz.socialnetwork.http.timeline;

import com.ezehertz.socialnetwork.app.cqbus.CqBus;
import com.ezehertz.socialnetwork.app.timeline.GetUserTimeline;
import com.ezehertz.socialnetwork.app.timeline.GetUserTimelineResult;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.UserNotFoundError;
import com.ezehertz.socialnetwork.http.tweets.TweetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/timeline")
public class TimelineController {

    private final CqBus bus;

    public TimelineController(CqBus bus) {
        this.bus = bus;
    }

    @GetMapping
    public ResponseEntity<?> getTimeline(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) Long cursor
    ) {
        try {
            GetUserTimelineResult result = bus.execute(new GetUserTimeline(Id.of(userId), limit, cursor));

            List<TimelineEntryResponse> entries = result.entries().stream()
                    .map(TimelineEntryResponse::fromTimelineEntry)
                    .toList();

            Map<String, TweetResponse> tweetsById = result.tweetsById().entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().rawId(),
                            e -> TweetResponse.fromTweet(e.getValue())
                    ));

            TimelineResponse response = new TimelineResponse(entries, result.nextCursor(), tweetsById);

            return ResponseEntity.ok(response);
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
