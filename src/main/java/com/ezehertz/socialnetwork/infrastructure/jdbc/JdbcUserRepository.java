package com.ezehertz.socialnetwork.infrastructure.jdbc;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.users.User;
import com.ezehertz.socialnetwork.domain.users.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbc;

    public JdbcUserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<User> MAPPER = (rs, rowNum) -> User.create(
            Id.of(rs.getString("id")),
            rs.getString("username"),
            rs.getString("password"),
            rs.getLong("created_at")
    );

    @Override
    public void save(User user) {
        jdbc.update("""
                        INSERT INTO users(id, username, password, created_at) 
                        VALUES (?, ?, ?, ?)
                        ON CONFLICT (id) DO UPDATE SET username = EXCLUDED.username, created_at = EXCLUDED.created_at
                        """,
                user.id().rawId(),
                user.username(),
                user.password(),
                user.createdAt()
        );
    }

    @Override
    public Optional<User> findById(Id<User> id) {
        try {
            User user = jdbc.queryForObject(
                    "SELECT id, username, password, created_at FROM users WHERE id = ?",
                    MAPPER,
                    id.rawId()
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?",
                Integer.class,
                username
        );
        return count != null && count > 0;
    }
}
