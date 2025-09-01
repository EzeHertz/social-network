package com.ezehertz.socialnetwork.domain;


import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.InvalidUserError;
import com.ezehertz.socialnetwork.domain.users.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void shouldCreateUserWhenDataIsValid() {
        Id<User> id = Id.of("U1");
        User user = User.create(id, "bob", "secret", System.currentTimeMillis());

        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(id);
        assertThat(user.username()).isEqualTo("bob");
        assertThat(user.password()).isEqualTo("secret");
    }

    @Test
    void should_throw_errorWhenIdIsNull() {
        assertThatThrownBy(() -> User.create(null, "bob", "secret", System.currentTimeMillis()))
                .isInstanceOf(InvalidUserError.class)
                .hasMessage("User id cannot be null");
    }

    @Test
    void should_throw_errorWhenUsernameIsNull() {
        assertThatThrownBy(() -> User.create(Id.of("U1"), null, "secret", System.currentTimeMillis()))
                .isInstanceOf(InvalidUserError.class)
                .hasMessage("Username cannot be empty");
    }

    @Test
    void should_throw_errorWhenUsernameIsBlank() {
        assertThatThrownBy(() -> User.create(Id.of("U1"), "   ", "secret", System.currentTimeMillis()))
                .isInstanceOf(InvalidUserError.class)
                .hasMessage("Username cannot be empty");
    }

    @Test
    void should_throw_errorWhen_passwordIsNull() {
        assertThatThrownBy(() -> User.create(Id.of("U1"), "bob", null, System.currentTimeMillis()))
                .isInstanceOf(InvalidUserError.class)
                .hasMessage("Password cannot be empty");
    }

    @Test
    void should_throw_errorWhen_passwordIsBlank() {
        assertThatThrownBy(() -> User.create(Id.of("U1"), "bob", "   ", System.currentTimeMillis()))
                .isInstanceOf(InvalidUserError.class)
                .hasMessage("Password cannot be empty");
    }
}
