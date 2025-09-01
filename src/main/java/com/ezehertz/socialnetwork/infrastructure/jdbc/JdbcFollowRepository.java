package com.ezehertz.socialnetwork.infrastructure.jdbc;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.follow.FollowRepository;
import com.ezehertz.socialnetwork.domain.users.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcFollowRepository implements FollowRepository {
    private final JdbcTemplate jdbc;

    public JdbcFollowRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public void follow(Id<User> follower, Id<User> followee) {
        jdbc.update("insert into follows(follower_id, followee_id, created_at) values (?,?,?) on conflict do nothing",
                follower.rawId(), followee.rawId(), System.currentTimeMillis());
    }


    @Override
    public void unfollow(Id<User> follower, Id<User> followee) {
        jdbc.update("delete from follows where follower_id = ? and followee_id = ?",
                follower.rawId(), followee.rawId());
    }


    @Override
    public List<Id<User>> findFollowerIds(Id<User> authorId) {
        return jdbc.query("select follower_id from follows where followee_id = ?",
                (rs, rn) -> Id.of(rs.getString(1)), authorId.rawId());
    }


    @Override
    public long countFollowers(Id<User> authorId) {
        Long c = jdbc.queryForObject("select count(*) from follows where followee_id = ?", Long.class, authorId.rawId());
        return c == null ? 0L : c;
    }
}
