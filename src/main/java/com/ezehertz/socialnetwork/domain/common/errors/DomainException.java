package com.ezehertz.socialnetwork.domain.common.errors;

public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
