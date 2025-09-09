package org.example.service;

import org.example.dao.SportDao;
import org.example.model.Sport;
import java.sql.SQLException;
import java.util.List;

public class SportService {
    private final SportDao sportDao = new SportDao();

    public List<Sport> getAllSports() throws SQLException {
        return sportDao.findAll();
    }

    public Sport getSportById(long id) throws SQLException {
        return sportDao.findById(id);
    }

    public void saveSport(Sport sport) throws SQLException {
        if (sport.getId() == null) {
            // Create new sport
            sportDao.create(sport);
        } else {
            // Update existing sport
            sportDao.update(sport);
        }
    }

    public void deleteSport(long id) throws SQLException {
        sportDao.delete(id);
    }
}