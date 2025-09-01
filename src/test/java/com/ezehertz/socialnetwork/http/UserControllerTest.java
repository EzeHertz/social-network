package com.ezehertz.socialnetwork.http;

import com.ezehertz.socialnetwork.app.cqbus.CqBus;
import com.ezehertz.socialnetwork.app.users.CreateUser;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.InvalidUserError;
import com.ezehertz.socialnetwork.domain.users.UserAlreadyExistsError;
import com.ezehertz.socialnetwork.http.users.UserController;
import com.ezehertz.socialnetwork.http.users.UserRequest;
import com.ezehertz.socialnetwork.http.users.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private CqBus bus;
    private UserController controller;

    @Test
    void shouldPassCorrectCommandToBus() {
        var req = new UserRequest("alice", "secret");
        var userId = Id.of("U1");
        when(bus.execute(any())).thenReturn(userId);

        controller.post(req);

        verify(bus).execute(new CreateUser("alice", "secret"));
    }

    @Test
    void shouldCreateUserWhenValidRequest() {
        var req = new UserRequest("alice", "secret");
        var userId = Id.of("U1");
        when(bus.execute(any())).thenReturn(userId);

        var response = controller.post(req);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(UserResponse.class);
        var body = (UserResponse) response.getBody();
        assertThat(body.id()).isEqualTo("U1");
        assertThat(body.username()).isEqualTo("alice");
    }

    @Test
    void shouldReturnConflictWhenUserAlreadyExists() {
        var req = new UserRequest("alice", "secret");
        when(bus.execute(any(CreateUser.class))).thenThrow(new UserAlreadyExistsError("User alice already exists"));

        var response = controller.post(req);

        assertThat(response.getStatusCode().value()).isEqualTo(409);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("user_already_exists");
        assertThat(body.get("message")).isEqualTo("User alice already exists");
    }

    @Test
    void shouldReturnBadRequestWhenUserIsInvalid() {
        var req = new UserRequest("", "pass123");
        when(bus.execute(any(CreateUser.class))).thenThrow(new InvalidUserError("Username cannot be empty"));

        var response = controller.post(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody())
                .isInstanceOf(Map.class)
                .satisfies(body -> {
                    Map<?, ?> map = (Map<?, ?>) body;
                    assertThat(map.get("error")).isEqualTo("invalid_user");
                    assertThat(map.get("message")).isEqualTo("Username cannot be empty");
                });
    }

    @BeforeEach
    void setUp() {
        bus = mock(CqBus.class);
        controller = new UserController(bus);
    }
}
