package com.ezehertz.socialnetwork.app.follows;

import com.ezehertz.socialnetwork.app.cqbus.Command;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.User;

public record FollowUser(Id<User> follower, Id<User> followee) implements Command<Void> {}
