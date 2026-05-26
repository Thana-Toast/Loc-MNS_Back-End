package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.Loan;
import fr.ctanchot.locmns.model.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanDTO toDto(Loan entity) {
        if (entity == null) return null;

        LoanDTO dto = new LoanDTO();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setActualReturnDate(entity.getActualReturnDate());

        if (entity.getLoanRequest() != null) {
            dto.setLoanRequestId(entity.getLoanRequest().getId());
            if (entity.getLoanRequest().getEquipment() != null) {
                dto.setEquipmentSerialNumber(entity.getLoanRequest().getEquipment().getSerialNumber());
            }
            if (entity.getLoanRequest().getAppUser() != null) {
                dto.setAppUserFullName(entity.getLoanRequest().getAppUser().getFirstName() + " " + entity.getLoanRequest().getAppUser().getLastName());
            }
        }

        return dto;
    }

    public Loan toEntity(LoanDTO dto) {
        if (dto == null) return null;

        Loan entity = new Loan();
        entity.setId(dto.getId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setActualReturnDate(dto.getActualReturnDate());

        if (dto.getLoanRequestId() != null) {
            LoanRequest request = new LoanRequest();
            request.setId(dto.getLoanRequestId());
            entity.setLoanRequest(request);
        }

        return entity;
    }
}

