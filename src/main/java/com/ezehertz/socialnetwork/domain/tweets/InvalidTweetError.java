package com.ezehertz.socialnetwork.domain.tweets;

import com.ezehertz.socialnetwork.domain.common.errors.DomainException;

public class InvalidTweetError extends DomainException {
    public InvalidTweetError(String message) {
        super(message);
    }
}
