package com.ezehertz.socialnetwork.http.users;

import com.ezehertz.socialnetwork.app.cqbus.CqBus;
import com.ezehertz.socialnetwork.app.users.CreateUser;
import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.InvalidUserError;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserAlreadyExistsError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CqBus bus;

    public UserController(CqBus bus) {
        this.bus = bus;
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody UserRequest req) {
        try {
            Id<User> id = bus.execute(new CreateUser(req.username(), req.password()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UserResponse(id.rawId(), req.username()));
        } catch (UserAlreadyExistsError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "user_already_exists", "message", e.getMessage()));
        } catch (InvalidUserError e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "invalid_user", "message", e.getMessage()));
        }
    }

}
