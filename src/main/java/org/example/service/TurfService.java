package org.example.service;

import org.example.dao.TurfDao;
import org.example.model.Turf;
import java.sql.SQLException;
import java.util.List;

public class TurfService {
    private final TurfDao turfDao = new TurfDao();

    public List<Turf> getAllTurfs() throws SQLException {
        return turfDao.findAll();
    }

    public Turf getTurfById(long id) throws SQLException {
        return turfDao.findById(id);
    }

    public void saveTurf(Turf turf, List<Long> sportIds) throws SQLException {
        if (turf.getId() == null) {
            // Create new turf
            turfDao.create(turf, sportIds);
        } else {
            // Update existing turf
            turfDao.update(turf, sportIds);
        }
    }

    public void deleteTurf(long id) throws SQLException {
        // We change the business logic here to deactivate instead of permanently deleting.
        turfDao.updateActiveStatus(id, false);
    }

    public List<Long> getSportIdsForTurf(long turfId) throws SQLException {
        return turfDao.findSportIdsForTurf(turfId);
    }
}