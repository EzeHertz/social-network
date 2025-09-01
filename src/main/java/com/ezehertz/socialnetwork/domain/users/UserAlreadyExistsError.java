package com.ezehertz.socialnetwork.domain.users;

import com.ezehertz.socialnetwork.domain.common.errors.DomainException;

public class UserAlreadyExistsError extends DomainException {
    public UserAlreadyExistsError(String message) {
        super(message);
    }
}
