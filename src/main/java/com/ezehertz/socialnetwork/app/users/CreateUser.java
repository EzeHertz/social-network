package com.ezehertz.socialnetwork.app.users;

import com.ezehertz.socialnetwork.app.cqbus.Command;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.User;

public record CreateUser(String username, String password) implements Command<Id<User>> {}
