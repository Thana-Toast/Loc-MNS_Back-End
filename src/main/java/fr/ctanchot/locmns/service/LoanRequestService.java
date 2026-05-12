package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.LoanRequestDao;
import fr.ctanchot.locmns.model.LoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanRequestService {

    private final LoanRequestDao loanRequestDao;

    @Autowired
    public LoanRequestService(LoanRequestDao loanRequestDao) {
        this.loanRequestDao = loanRequestDao;
    }

    public List<LoanRequest> findAll() {
        return loanRequestDao.findAll();
    }

    public Optional<LoanRequest> findById(Long id) {
        return loanRequestDao.findById(id);
    }

    public LoanRequest save(LoanRequest loanRequest) {
        if (loanRequest.getId() == null) {
            loanRequest.setDate(LocalDateTime.now());
        }
        return loanRequestDao.save(loanRequest);
    }

    public void deleteById(Long id) {
        loanRequestDao.deleteById(id);
    }

    public LoanRequest update(Long id, LoanRequest loanRequestUpdate) {
        Optional<LoanRequest> existingRequest = loanRequestDao.findById(id);
        if (existingRequest.isPresent()) {
            loanRequestUpdate.setId(id);
            loanRequestUpdate.setDate(existingRequest.get().getDate());
            return loanRequestDao.save(loanRequestUpdate);
        }
        return null;
    }
}
