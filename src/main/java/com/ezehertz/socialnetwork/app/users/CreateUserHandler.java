package com.ezehertz.socialnetwork.app.users;

import com.ezehertz.socialnetwork.app.cqbus.CommandHandler;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.common.id.IdGenerator;
import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserAlreadyExistsError;
import com.ezehertz.socialnetwork.domain.users.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateUserHandler implements CommandHandler<CreateUser, Id<User>> {
    private final RepositoryFactory repositories;
    private final IdGenerator idGenerator;

    public CreateUserHandler(RepositoryFactory repositories, IdGenerator idGenerator) {
        this.repositories = repositories;
        this.idGenerator = idGenerator;
    }

    @Override
    public Id<User> handle(CreateUser cmd) {
        UserRepository users = repositories.create(UserRepository.class);
        if (users.existsByUsername(cmd.username())) {
            System.out.println("User " + cmd.username() + " already exists");
            throw new UserAlreadyExistsError(cmd.username());
        }
        long now = System.currentTimeMillis();
        Id<User> id = Id.of(idGenerator.newId());
        User t = User.create(id, cmd.username(), cmd.password(), now);
        users.save(t);
        return id;
    }

    @Override
    public Class<CreateUser> commandType() {
        return CreateUser.class;
    }
}
