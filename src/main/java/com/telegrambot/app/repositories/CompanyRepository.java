package com.telegrambot.app.repositories;

import com.telegrambot.app.model.legalentity.Company;
import com.telegrambot.app.repositories.reference.LegalEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends LegalEntityRepository<Company> {
}
