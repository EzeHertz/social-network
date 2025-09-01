package com.ezehertz.socialnetwork.domain.users;

import com.ezehertz.socialnetwork.domain.common.errors.DomainException;

public class InvalidUserError extends DomainException {
    public InvalidUserError(String message) {
        super(message);
    }
}
