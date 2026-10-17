package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikesRowMapper implements RowMapper<Like> {
    @Nullable
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Like(rs.getLong("USER_ID"), rs.getLong("FILM_ID"));
    }
}
