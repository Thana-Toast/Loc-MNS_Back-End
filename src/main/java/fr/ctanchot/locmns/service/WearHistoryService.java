package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.WearHistoryDao;
import fr.ctanchot.locmns.model.WearHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WearHistoryService {

    private final WearHistoryDao wearHistoryDao;

    @Autowired
    public WearHistoryService(WearHistoryDao wearHistoryDao) {
        this.wearHistoryDao = wearHistoryDao;
    }

    public List<WearHistory> findAll() {
        return wearHistoryDao.findAll();
    }

    public Optional<WearHistory> findById(Long id) {
        return wearHistoryDao.findById(id);
    }

    public WearHistory save(WearHistory history) {
        if (history.getWearChangeDate() == null) {
            history.setWearChangeDate(LocalDateTime.now());
        }
        return wearHistoryDao.save(history);
    }

    public void deleteById(Long id) {
        wearHistoryDao.deleteById(id);
    }
}
