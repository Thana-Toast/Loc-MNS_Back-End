package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.LoanDao;
import fr.ctanchot.locmns.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanDao loanDao;

    @Autowired
    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    public List<Loan> findAll() {
        return loanDao.findAll();
    }

    public Optional<Loan> findById(Long id) {
        return loanDao.findById(id);
    }

    public Loan save(Loan loan) {
        return loanDao.save(loan);
    }

    public void deleteById(Long id) {
        loanDao.deleteById(id);
    }

    public Loan update(Long id, Loan loanUpdate) {
        Optional<Loan> existingLoan = loanDao.findById(id);
        if (existingLoan.isPresent()) {
            loanUpdate.setId(id);
            return loanDao.save(loanUpdate);
        }
        return null;
    }
}
