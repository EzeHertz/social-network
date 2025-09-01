package com.ezehertz.socialnetwork.domain.users;

import com.ezehertz.socialnetwork.domain.common.id.Id;

public final class User {
    private final Id<User> id;
    private final String username;
    private final String password;
    private final long createdAt;


    private User(Id<User> id, String username, String password, long createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
    }

    public static User create(Id<User> id, String username, String password, long createdAt) {
        validate(id, username, password);
        return new User(id, username, password, createdAt);
    }

    public Id<User> id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String password() { return password; }

    public long createdAt() {
        return createdAt;
    }

    private static void validate(Id<User> id, String username, String password) {
        if (id == null) {
            throw new InvalidUserError("User id cannot be null");
        }
        if (username == null || username.isBlank()) {
            throw new InvalidUserError("Username cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new InvalidUserError("Password cannot be empty");
        }
    }
}
