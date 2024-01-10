package com.supportbot.repositories.command;

import com.supportbot.model.reference.legalentity.Company;
import com.supportbot.repositories.reference.LegalEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends LegalEntityRepository<Company> {
}
