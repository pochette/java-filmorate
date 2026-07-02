package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MPARowMapper implements RowMapper<MPA> {
    @Nullable
    @Override
    public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MPA(rs.getInt("MPA_ID"), rs.getString("MPA_NAME"));
    }
}
