package com.ezehertz.socialnetwork.app.tweets;

import com.ezehertz.socialnetwork.app.cqbus.Command;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.users.User;

public record PostTweet(Id<User> authorId, String content) implements Command<Id<Tweet>> {}
