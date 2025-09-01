package com.ezehertz.socialnetwork.app.timeline;

import com.ezehertz.socialnetwork.app.cqbus.Command;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.User;

public record GetUserTimeline(Id<User> userId, int limit, Long cursorCreatedAt) implements Command<GetUserTimelineResult> {
}
