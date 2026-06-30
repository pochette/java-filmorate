package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class MpaRepository extends BaseRepository<MPA> implements MpaStorage {
    private static final String QUERY_GET_MPA_BY_ID = "SELECT * FROM MPA_TYPE WHERE MPA_ID = ?";
    private static final String QUERY_GET_ALL_MPA = "SELECT * FROM MPA_TYPE";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper);
    }


    @Override
    public MPA getMpaById(Integer id) {
        return findOne(QUERY_GET_MPA_BY_ID, id);
    }

    @Override
    public List<MPA> getAllMpa() {
        return findMany(QUERY_GET_ALL_MPA);
    }
}
