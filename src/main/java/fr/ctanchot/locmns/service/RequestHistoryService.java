package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.RequestHistoryDao;
import fr.ctanchot.locmns.model.RequestHistory;
import fr.ctanchot.locmns.model.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RequestHistoryService {

    private final RequestHistoryDao requestHistoryDao;

    @Autowired
    public RequestHistoryService(RequestHistoryDao requestHistoryDao) {
        this.requestHistoryDao = requestHistoryDao;
    }

    public List<RequestHistory> findAll() {
        return requestHistoryDao.findAll();
    }

    public Optional<RequestHistory> findById(Long id) {
        return requestHistoryDao.findById(id);
    }

    public RequestHistory save(RequestHistory history) {
        if (history.getStatusChangeDate() == null) {
            history.setStatusChangeDate(LocalDateTime.now());
        }
        return requestHistoryDao.save(history);
    }

    public void deleteById(Long id) {
        requestHistoryDao.deleteById(id);
    }
}
