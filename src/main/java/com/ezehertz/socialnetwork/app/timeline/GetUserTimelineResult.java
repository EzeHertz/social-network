package com.ezehertz.socialnetwork.app.timeline;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.timeline.TimelineEntry;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;

import java.util.List;
import java.util.Map;

public record GetUserTimelineResult(
        List<TimelineEntry> entries,
        Long nextCursor,
        Map<Id<Tweet>, Tweet> tweetsById
) { }
