package com.ezehertz.socialnetwork.domain.common.repositoryFactory;

public interface RepositoryFactory {
    <T> T create(Class<T> repositoryClass);
}
