package com.ezehertz.socialnetwork.infrastructure.jdbc;

import com.ezehertz.socialnetwork.domain.common.id.Id;
import com.ezehertz.socialnetwork.domain.tweets.Tweet;
import com.ezehertz.socialnetwork.domain.tweets.TweetRepository;
import com.ezehertz.socialnetwork.domain.users.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcTweetRepository implements TweetRepository {
    private final JdbcTemplate jdbc;

    public JdbcTweetRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public void save(Tweet t) {
        jdbc.update("insert into tweets(id, author_id, text, created_at) values (?,?,?,?)",
                t.id().rawId(), t.authorId().rawId(), t.content(), t.createdAt());
    }


    @Override
    public Optional<Tweet> findById(Id<Tweet> id) {
        var list = jdbc.query("select id, author_id, text, created_at from tweets where id = ?", mapper(), id.rawId());
        return list.stream().findFirst();
    }


    @Override
    public Map<Id<Tweet>, Tweet> findByIds(Collection<Id<Tweet>> ids) {
        if (ids == null || ids.isEmpty()) return Map.of();
        var idStrings = ids.stream().map(Id::rawId).toList();
        String inSql = idStrings.stream().map(s -> "?").collect(Collectors.joining(","));
        var list = jdbc.query("select id, author_id, text, created_at from tweets where id in (" + inSql + ")", mapper(), idStrings.toArray());
        Map<Id<Tweet>, Tweet> map = new HashMap<>();
        for (var t : list) map.put(t.id(), t);
        return map;
    }


    @Override
    public List<Tweet> findByAuthor(Id<User> authorId, int limit, String cursor) {
        return jdbc.query("select id, author_id, text, created_at from tweets where author_id = ? order by created_at desc limit ?",
                mapper(), authorId.rawId(), limit);
    }


    private RowMapper<Tweet> mapper() {
        return (rs, rn) -> mapRow(rs);
    }

    private static Tweet mapRow(ResultSet rs) throws SQLException {
        return Tweet.create(Id.of(rs.getString("id")), Id.of(rs.getString("author_id")),
                rs.getString("text"), rs.getLong("created_at"));
    }
}
