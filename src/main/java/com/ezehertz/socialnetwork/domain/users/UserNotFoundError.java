package com.ezehertz.socialnetwork.domain.users;

import com.ezehertz.socialnetwork.domain.common.errors.DomainException;

public class UserNotFoundError extends DomainException {
    public UserNotFoundError(String message) {
        super(message);
    }
}
