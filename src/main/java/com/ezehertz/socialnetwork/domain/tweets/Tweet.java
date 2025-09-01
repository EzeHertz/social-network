package com.ezehertz.socialnetwork.domain.tweets;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.User;

public final class Tweet {
    private final Id<Tweet> id;
    private final Id<User> authorId;
    private final String content;
    private final long createdAt;


    private Tweet(Id<Tweet> id, Id<User> authorId, String content, long createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Tweet create(Id<Tweet> id, Id<User> authorId, String content, long createdAt) {
        validate(id, authorId, content);
        return new Tweet(id, authorId, content, createdAt);
    }

    public Id<Tweet> id() {
        return id;
    }

    public Id<User> authorId() {
        return authorId;
    }

    public String content() {
        return content;
    }

    public long createdAt() {
        return createdAt;
    }

    private static void validate(Id<Tweet> id, Id<User> authorId, String content) {
        if (id == null) {
            throw new InvalidTweetError("Tweet id cannot be null");
        }
        if (authorId == null) {
            throw new InvalidTweetError("Author id cannot be null");
        }
        if (content == null || content.isBlank()) {
            throw new InvalidTweetError("Content cannot be empty");
        }
    }
}
