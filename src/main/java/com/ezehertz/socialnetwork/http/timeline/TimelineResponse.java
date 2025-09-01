package com.ezehertz.socialnetwork.http.timeline;

import com.ezehertz.socialnetwork.http.tweets.TweetResponse;

import java.util.List;
import java.util.Map;

public record TimelineResponse(
        List<TimelineEntryResponse> entries,
        Long nextCursor,
        Map<String, TweetResponse> tweetsById
) {}
