package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BaseRepository<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected void batchUpdateBase(String sql, BatchPreparedStatementSetter pss) {
        int[] batchRow = jdbc.batchUpdate(sql, pss);
        if (batchRow.length == 0) {
            throw new RuntimeException("Не удалось обновить данные по запросу: " + sql);
        }

    }

    protected boolean delete(String sql, Object... params) {
        int rowUpdated = jdbc.update(sql, params);
        return rowUpdated > 0;
    }

    protected boolean deleteAll(String sql) {
        int rowUpdated = jdbc.update(sql);
        return rowUpdated > 0;

    }

    protected List<T> findMany(String sql, Object... params) {
        return jdbc.query(sql, mapper, params);
    }

    protected T findOne(String sql, Object... params) {
        List<T> list = jdbc.query(sql, mapper, params);
        if (list.isEmpty()) {
            throw new EntityNotFoundException("Не удалось найти данные по запросу: " + sql);
        }
        return list.getFirst();
    }

    //Возвращает LONG id создаваемой сущности
    protected Long insert(String sql, String idName, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    new String[]{idName});
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps;
        }, keyHolder);
        Number id = keyHolder.getKey();
        if (null == id) {
            throw new RuntimeException("Не удалось сохранить данные");
        } else
            return id.longValue();
    }

    protected <R> R query(String sql,
                          ResultSetExtractor<R> extractor,
                          Object... params) {
        return jdbc.query(sql, extractor, params);
    }

    protected boolean update(String sql, Object... params) {
        int rowUpdated = jdbc.update(sql, params);
        return rowUpdated > 0;
    }

    protected String placeholders(int count) {
        return String.join(",", Collections.nCopies(count, "?"));
    }
}
