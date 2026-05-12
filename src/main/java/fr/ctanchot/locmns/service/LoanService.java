package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.LoanDao;
import fr.ctanchot.locmns.model.Loan;
import fr.ctanchot.locmns.model.LoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        if (isEquipmentAvailable(loan)) {
            return loanDao.save(loan);
        } else {
            throw new RuntimeException("L'équipement n'est pas disponible pour cette période (conflit de dates).");
        }
    }

    public void deleteById(Long id) {
        loanDao.deleteById(id);
    }

    public Loan update(Long id, Loan loanUpdate) {
        Optional<Loan> existingLoan = loanDao.findById(id);
        if (existingLoan.isPresent()) {
            loanUpdate.setId(id);
            if (isEquipmentAvailable(loanUpdate)) {
                return loanDao.save(loanUpdate);
            } else {
                throw new RuntimeException("Mise à jour impossible : conflit de dates pour cet équipement.");
            }
        }
        return null;
    }

    private boolean isEquipmentAvailable(Loan newLoan) {
        LoanRequest request = newLoan.getLoanRequest();
        if (request == null || request.getEquipment() == null) {
            return true;
        }

        Long equipmentId = request.getEquipment().getId();
        LocalDate start = newLoan.getStartDate();
        LocalDate end = newLoan.getEndDate();

        List<Loan> existingLoans = loanDao.findAll().stream()
                .filter(l -> l.getLoanRequest().getEquipment().getId().equals(equipmentId))
                .filter(l -> !l.getId().equals(newLoan.getId()))
                .toList();

        for (Loan existing : existingLoans) {
            boolean overlap = !start.isAfter(existing.getEndDate()) && !end.isBefore(existing.getStartDate());
            if (overlap) {
                return false;
            }
        }

        return true;
    }
}
