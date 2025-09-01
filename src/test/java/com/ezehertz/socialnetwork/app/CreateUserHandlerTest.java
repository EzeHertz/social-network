package com.ezehertz.socialnetwork.app;

import com.ezehertz.socialnetwork.app.users.CreateUser;
import com.ezehertz.socialnetwork.app.users.CreateUserHandler;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.id.IdGenerator;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserAlreadyExistsError;
import com.ezehertz.socialnetwork.domain.users.UserRepository;
import com.ezehertz.socialnetwork.infrastructure.inMemory.InMemoryRepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateUserHandlerTest {

    private RepositoryFactory factory;
    private CreateUserHandler handler;

    @Test
    void handleShouldCreateUserWhenUsernameNotExists() {
        var users = factory.create(UserRepository.class);
        CreateUser cmd = new CreateUser("alice", "password");

        Id<User> id = handler.handle(cmd);

        assertThat(users.findById(id)).isNotNull();
    }

    @Test
    void handleShouldThrowExceptionWhenUsernameExists() {
        var users = factory.create(UserRepository.class);
        users.save(User.create(Id.of("A1"), "alice", "password", 1L));

        CreateUser cmd = new CreateUser("alice", "password");

        assertThrows(UserAlreadyExistsError.class, () -> handler.handle(cmd));
    }

    @BeforeEach
    void setUp() {
        factory = new InMemoryRepositoryFactory();
        IdGenerator ids = () -> "T-1";
        handler = new CreateUserHandler(factory, ids);
    }
}
