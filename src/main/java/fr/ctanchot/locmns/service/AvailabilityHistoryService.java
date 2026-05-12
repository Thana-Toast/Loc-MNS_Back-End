package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AvailabilityHistoryDao;
import fr.ctanchot.locmns.model.AvailabilityHistory;
import fr.ctanchot.locmns.model.AvailabilityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityHistoryService {

    private final AvailabilityHistoryDao availabilityHistoryDao;

    @Autowired
    public AvailabilityHistoryService(AvailabilityHistoryDao availabilityHistoryDao) {
        this.availabilityHistoryDao = availabilityHistoryDao;
    }

    public List<AvailabilityHistory> findAll() {
        return availabilityHistoryDao.findAll();
    }

    public Optional<AvailabilityHistory> findById(Long id) {
        return availabilityHistoryDao.findById(id);
    }

    /**
     * Ajoute un nouvel état de disponibilité et clôture automatiquement le précédent (endDate).
     */
    @Transactional
    public AvailabilityHistory changeStatus(Long equipmentId, AvailabilityStatus status) {
        availabilityHistoryDao.findAll().stream()
                .filter(h -> h.getEquipment().getId().equals(equipmentId) && h.getEndDate() == null)
                .forEach(h -> {
                    h.setEndDate(LocalDateTime.now());
                    availabilityHistoryDao.save(h);
                });

        AvailabilityHistory newHistory = new AvailabilityHistory();
        return null;
    }
    
    public AvailabilityHistory save(AvailabilityHistory history) {
        if (history.getStartDate() == null) {
            history.setStartDate(LocalDateTime.now());
        }
        return availabilityHistoryDao.save(history);
    }

    public void deleteById(Long id) {
        availabilityHistoryDao.deleteById(id);
    }
}
